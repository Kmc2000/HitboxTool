# HitboxTool
A primitive hitbox generator for NSV13 ships.

## Usage:
* Export your chosen ship image from your chosen image editing software. Ensure that it's facing north.
* Open up the tool, and in the file selection dialogue, choose the file you want to edit.
![file dialogue](https://cdn.discordapp.com/attachments/593077519247474689/701250519901011989/unknown.png "File selection example")
* The editor window will pop open if you chose a valid image file (Currently .jpg or .png) that you have access to. You'll see the following:
![edit window 1](https://cdn.discordapp.com/attachments/593077519247474689/701251000417124403/unknown.png "Editor window before making changes")
* Now you've got the editor up, simply click with your mouse to place squares, which will represent the points of the hitbox. They'll be joined with lines so you can see how the hitbox will behave.
![edit window 2](https://cdn.discordapp.com/attachments/593077519247474689/701251355330740294/unknown.png "An example hitbox")
* Place the points in a _counter clockwise_ direction (as shown on the wheel to the top left) until you have a suitable bounding hitbox. Then click the button in the top left to get your outputted code.
* Finally, copy paste the code it gives you onto your overmap object. This should look similar to:

/obj/structure/example
	name = "example"
	collision_positions = list(new /datum/vector2d(-13,71), new /datum/vector2d(-25,52), new /datum/vector2d(-24,-25), new /datum/vector2d(-11,-66), new /datum/vector2d(4,-69), new /datum/vector2d(15,-28), new /datum/vector2d(15,38), new /datum/vector2d(6,61))

This tool is still rather primitive, and does not scale images at all. Bear this in mind when using it.
This hitbox generator is only intended for use with NSV13's "Qwer2d" physics system. This is not a generic hitbox tool.
