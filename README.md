# Osprey

## Osprey is a Client-Server Application written in Java. It is also a bird of prey:
![Osprey](https://www.allaboutbirds.org/guide/PHOTO/LARGE/osprey_1.jpg)

* __Osprey__ client interfaces with the host machine's built-in microphone and calculates the sound of the environment.
    * If the sound exceeds a certain threshold, the client informs the server
    
* __Osprey server__ creates a thread which establishes an SMTP connection to Google's SMTP server and sends configured recipients an email informing them to be quiet. 

This project was developed by Brennan McFarland and Emilio Lopez for Kent Hack Enough 2017. 


## Notes
Due to laziness, the source code on Github contains the password to one of Emilio's emails. That will be changed very promptly. 


