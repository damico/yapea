yapea
=====

Yet Another Picture Encryption Application

=====
What is it? 

A small and simple Android application for image encryption. Yapea is free and open-source (GPL v2). It was developed at TIX11 R&D lab.

=====
Requirements:

Android >= 4.0

=====
Encryption Algorithms used in Yapea:

Symetric encryption:

- AES (CBC/PKCS5Padding)
- Blowfish (CFB/NoPadding)

The Initialization Vectors are generated based on unique data from the smartphone.

=====
Encryption key used in Yapea:

- Length: 256 bits

Generated through key derivation (from user-defined password) with PBKF2 algorithm. The salt are generated based on unique data from the smartphone.
The key is stored inside a configuration file, at smartphone file system. This file is used for password verification at first time of application use. After that the key is encripted and stored inside smartphone memory (cache). But at anytime the user can choose to delete the encrypted key from memory (Clear cache).

=====
Additional features:

- Application reset: At anytime the user can choose to dump ALL application data, including encrypted images and configuration.
- Panic password: A password that can be used to delete all encrypted images. In a case where user is forced to give its key. (If you’re traveling overseas, across borders or anywhere you’re afraid your smartphone might be tampered with or examined). 
- Languages: English and Portuguese

=====
Screen capture:

https://picasaweb.google.com/113557199789179991340/Yapea?authuser=0&feat=directlink

=====
Author:

José Ricardo de Oliveira Damico <damico@tix11.com>
