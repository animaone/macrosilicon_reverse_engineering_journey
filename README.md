
### warning! Don't install this .apk file if you don't know what you are doing. I am absolutely NOT SURE if it is from official sources!
  


# reverse-engineering a usb-to-vga adapter from Macrosilicon


  

# [Round I]

![image](https://user-images.githubusercontent.com/31348553/141840657-444c6f33-fbc0-47b8-8229-1087a71cb1d0.png)


Everything started when I bought the device. I wanted to get something more related to fl2k/fl2000 chipset, but as most clones you will find in the wild, there will not be a lot of them with fl2000 chipset.

But, instead of trowing it away I decided to do something, maybe trying to make a driver for Linux.

Started googling some information about it and got its usb properties:    __ID 534d:6021 MACROSILICON usb extscreen__

I captured the USB packets and tried to use the USB with python without success.

It was when I found the github repository for __ms210x-tools__, which had some functions that allowed me to read and write specific bytes of the device memory. Also found the device has an 8051 micro-controller (maybe more than one?)

Yes? I just wanted to play with a driver and suddenly found I could write in the device memory... Amazing!

Now, let the game begin.

It didn't took to much time for I to find that the device had some memory areas that couldn't be written. After some time playing, I did get the following memory map:

* 0x0000 - 0xbfff -> read-only memory
* 0xc000 - 0xdfff -> writable ram memory
* 0xe000 - 0xffff -> can/cannot be written it depends (registers or space for communication?)

It seems that all Macrosilicon devices share some type of similarities when it is related to data reading/writing, but... let's do something else...

I managed to dump the device code (will describe this other not-so-trivial trick later), done some reverse-engineering of its routines, and... then I suddenly found a driver for it, distributed by two Chinese sellers, and also an older version in a GitHub repository. I am leaving the apk files here if you have some curiosity for installing it in your phone (warning!). I tested the driver without success (it runs for some time with a bit o delay, and if I try to rotate the screen, it freezes... Seems to be a hardware-related bug..., bad news: the driver is buggy :( )

These apks could them be decompiled (and yes, I am leaving the most important part of its source code here). You can also extract it with Jadx if you want (I don't recommend trying to recompile it, it may become a long-term project, try to edit the .smali files with apktool instead).

Having the source helped to understand the main routines of the USB driver, and figuring out why my approach was not working(or not)...

![image](https://user-images.githubusercontent.com/31348553/141846163-ced9fa8a-6093-4691-944f-32ce3e60545b.png)


So... the device starts with some routines for reading the monitor configurations and uses the read and write prefixes for doing configurations 

* \xB5 [offset]
* \xB6 [offset]

After this it uses \xA6 + commands for setting video on, video input on, and power the monitor on

After this point, the frames start to be sent... (maybe there is some synchronization feedback going on here that I didn't figure out yet?)

As most persons that played with these devices have done their research with devices that convert VIDEO-TO-USB (instead of USB-TO-VIDEO), I don't have a lot of information about the protocol used for writing the video frames... (maybe some weird tile-based compressed data?)

But it is just a matter of time until I can figure out how to do it. Since the apk source code is here for helping.

When I see the assembly code, I just feel like I need to reach deeper levels of insanity, what If I can? ;)

# [Round II]

After some time looking around I have found the file RegisterMap.java in the apk decompilation. This file is more interesting than ever, and contains lots of "registers" which are like buttons for doing interesting things inside of the processor...
But... what should I do now?

![image](https://user-images.githubusercontent.com/31348553/142734625-ff17d98c-65a3-4709-9071-495fed70afee.png)

# [ADDED "DOCUMENTATION"]

![image](https://github.com/animaone/macrosilicon_reverse_engineering_journey/assets/31348553/9396119d-ab62-497e-84a1-d0931e47517b)

# [THE END:]

I think I had enought of this project, and stopped it a long time ago. This project is not maintained anymore. Feel free to thank everyone that developed software for this device, and search google and github before asking. Byeeee :)


# \[TO BE CONTINUED\]

