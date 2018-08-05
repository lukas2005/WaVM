package werewolvesAndVampires.vampires.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AIVampireAvoidSun extends EntityAIAvoid {

    public AIVampireAvoidSun(EntityCreature vampire, double speed) {
        super(vampire, speed);
    }

    @Override
    protected boolean shouldAvoidPosition(World world, BlockPos pos) {
        return world.canBlockSeeSky(pos) && world.isDaytime();
    }
}
