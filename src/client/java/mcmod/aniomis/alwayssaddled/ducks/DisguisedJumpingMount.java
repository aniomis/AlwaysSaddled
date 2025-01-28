package mcmod.aniomis.alwayssaddled.ducks;

import org.spongepowered.asm.mixin.Unique;

public interface DisguisedJumpingMount {
	@Unique
	void always_saddled_template_1_20_4$setJumpStrength(int strength);
}
