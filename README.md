# this repository will document my journey to reverse-engineer a usb-to-vga adapter from Macrosilicon

![image](https://user-images.githubusercontent.com/31348553/141840657-444c6f33-fbc0-47b8-8229-1087a71cb1d0.png)


Everything started when I bought the device. I wanted to get something more related to fl2k/fl2000 chipset, but as most clones you will find in the wild, there will not be a lot of them with fl2000 chipset.

But, instead of trowing it away I decided to do something, maybe trying to make a driver for linux.

Started googling some information about it and got its usb propperties:    __ID 534d:6021 MACROSILICON usb extscreen__

I captured the usb packets and tried to use the usb with python without success.

It was when I found the tools ms-dfjsdofj, which had some functiosn that allowed me to read and write specific bytes of the device memory. Also found the device has an 8051 microcontroller (maybe more than one?)

Yes? I just wanted to play with a driver and sudenlly found I could write in the device memory... Amazing!

Now, let the game begin.

It didn't took to much time for I to find that the device had some memory areas that couldn't be written. After some time playing I did get the following memory map:

0x0000 - 0xbfff -> read-only memory
0xc000 - 0xdfff -> writable ram memory
0xe000 - 0xffff -> can/cannot be written it depends (register/space for communication memory maybe?)

It seems that all Macrosilicon devices share some type of similarities when it is related to data reading/writing, but... lets do something else...

I managed to dump the device code (will describe this other not-so-trivial trick here), done some reverse-engineering of its routines, and... then I suddenly found a driver for it in 

# \[TO BE CONTINUED\]
