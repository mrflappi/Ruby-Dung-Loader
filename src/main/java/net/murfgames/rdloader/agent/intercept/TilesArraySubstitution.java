package net.murfgames.rdloader.agent.intercept;

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;

public class TilesArraySubstitution {

    public static AsmVisitorWrapper create() {
        return new AsmVisitorWrapper.ForDeclaredMethods()
                .method(ElementMatchers.isMethod().and(ElementMatchers.not(ElementMatchers.isConstructor())),
                        (instrumentedType, instrumentedMethod, methodVisitor, implementationContext, typePool, writerFlags, readerFlags) -> new TileFieldInterceptor(methodVisitor));
    }

    private static class TileFieldInterceptor extends MethodVisitor {
        // Track the count of pending Tile array references active on the bytecode execution stack
        private int pendingTileArrays = 0;

        public TileFieldInterceptor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            // Ensure the field is exactly what we are looking for
            if (opcode == Opcodes.GETSTATIC
                    && "com/mojang/minecraft/level/tile/Tile".equals(owner)
                    && "tiles".equals(name)
                    && "[Lcom/mojang/minecraft/level/tile/Tile;".equals(descriptor)) {

                super.visitFieldInsn(opcode, owner, name, descriptor);

                // Increment our tracking counter instead of setting a strict immediate flag
                this.pendingTileArrays++;
                return;
            }

            super.visitFieldInsn(opcode, owner, name, descriptor);
        }

        @Override
        public void visitInsn(int opcode) {
            // Intercept the array load instruction if we know a Tile array reference was previously pushed
            if (opcode == Opcodes.AALOAD && pendingTileArrays > 0) {
                this.pendingTileArrays--; // Consume one tracked reference

                // Swap to ensure 'tiles[]' array is at the top of stack
                super.visitInsn(Opcodes.SWAP);

                // Pop tiles[] off stack
                super.visitInsn(Opcodes.POP);

                // Invoke TileRegistry.getTile() instead
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "net/murfgames/rdloader/level/TileRegistry",
                        "getTile",
                        "(I)Lcom/mojang/minecraft/level/tile/Tile;",
                        false
                );
                return;
            }

            super.visitInsn(opcode);
        }
    }
}
