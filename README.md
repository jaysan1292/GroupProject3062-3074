#COMP 3062 / 3074 Group Project: "GBC Scavenger Hunt"
_____
##Group Members

* Jason Recillo
* Peter Le
* Mellicent Dres

##Project Modules

* `scavenger-core`: Contains common classes used by the other modules.
* `scavenger-service`: The web service that drives the whole system.
* `scavenger-web`: The scavenger hunt's administrative interface, where every aspect of the game can be managed.
* `scavenger-mobile`: The Android app that acts as the user interface for its players.

___

####To compile individual modules:

Each module depends on `scavenger-core`, and you have two choices:

	cd <project_dir>
	mvn -am -pl <module_name> <action>

This will compile the specified module, and any module it depends on, which in our case is `scavenger-core`. Output will be in `<module_name>/target`.

Or you can do:

	cd <project_dir>/scavenger-core
    mvn install
	cd ../<module_dir>
	mvn <action>

Your other choice is this, which will install `scavenger-core` into your local Maven repository, so that when you enter `mvn <action>` afterwards, it will be able to find the dependency (specifically, `com.jaysan1292.groupproject:scavenger-core:1.0-SNAPSHOT`) in the local repository.
