Always Saddled restores the ability to move when riding a horse disguised as another mob using a 
server plugin. Plugins like [EasyRider](https://github.com/NerdNu/EasyRider) by default hide the
horse disguise from the rider, since the minecraft client does not support controlling arbitrary
living entities as vehicles, other than Abstract Horses™.

Tested with EasyRider on PaperMC using Minecraft 1.20.4. Works best with mobs that are shaped
like a horse. Slim mobs that can bas through 1 block wide spaces will probably trigger anticheat.

Supported:

* Movement
* Jumping

Not yet supported:

* Horse inventory
* Horse collision model

How it works:

* Detects when the horse you are riding vanishes, saves its attributes (speed, jump).
* When the server makes you ride a new non-horse living entity in the same position, transfer the
  attributes to that mob.
* All mobs can now have a horse_jump attribute. If it's more than 0, show the horse bar and allow 
  jumping.
* Copy the controlled horse movement logic into all mobs.

Use at your own risk! This could definitely trigger some anti-cheat measures, as your client might
try to move in ways that are unhorselike, specially if you ride a small disguise.
