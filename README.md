# UYBHYS2022formindChallenge

This is the solution to the challenge published by [formind](https://twitter.com/Formind) on Twitter to win a voucher for [UYBHYS2022](https://www.unlockyourbrain.bzh/).

## The challenge

The challenged was posted [here](https://twitter.com/Formind/status/1582710135079997440)

>Plus de billets pour la conf @UYBHYS
 ? 😭 Pas de panique ! Le 1er à résoudre ce petit challenge gagne sa place ! 🤩 Envoyez en DM le 3e résultat de ce générateur sécurisé de mdp sachant que les 2 premiers ont leaké : 7EHPFygG2gq2, 7M7y9YTHt800 A vos claviers ! #cybersécurité

![javacode](https://pbs.twimg.com/media/FfbpCgYWIAEy3So?format=jpg&name=large)


## Solving the challenge

First we can notice that the password generator algorithm uses a very basic random number generator `java.util.Random`. Moreover it uses the same seed for the generation of the three passwords in a matter of speeding-up which is a very bad idea. Indeed, `java.util.Random` is a [Linear Congruential](https://en.wikipedia.org/wiki/Linear_congruential_generator) [PRNG](https://en.wikipedia.org/wiki/Pseudorandom_number_generator)

>The PRNG-generated sequence is not truly random, because it is completely determined by an initial value, called the PRNG's seed (which may include truly random values)

source: [Wikipedia](https://en.wikipedia.org/wiki/Pseudorandom_number_generator)

As explained in  [Cracking Random Number Generators](https://jazzy.id.au/2010/09/20/cracking_random_number_generators_part_1.html), you can guess the seed if you have two consecutive values produced by the same `java.util.Random` instance. If you look at the SecurePassword algorithm this is exactly what we have in one generated password, we even have more than 2 values, we'll see later. The password is built by concatenating the base36 values of two *random* 32 bits integers. As the sign has been removed we need to try the bruteforce-attack with 4 combinations of sign:

 1. v1 / v2
 2. -v1 / v2
 3. -v1 / -v2
 4. v1 / -v2

In our case, with `7EHPFygG2gq2` as a password, v1 would be `Integer.parseInt("7EHPFy", 36)` and v2 would be `Integer.parseInt("gG2gq2", 36)`.

When the seed has been bruteforced, we want to check that we're on the good way. To do so, we need to generated the second password we know which is ` 7M7V9YTHt800`. To do so we must get the random generator to the same state it was after the first password has been generated. This requires to produces additionnal *random* 32 bits integers before. Indeed, in the SecurePassword algorithm you can see that for each letter another *random* values is generated and will uppercase the letter if the value is even (`random.nextInt( ) % 2 == 0`).

Now we can generate a password and check that it matches the second password, if not we need to test another combination of signs. When we're good with the second password we can kindly ask for the third one.

See `BrokenPassword.java` for more details on the hack.

## How to get the source code

Just clone this repo

    git clone https://github.com/cdeletre/UYBHYS2022formindChallenge.git

## How to build SecurePassword generator ?

Get you favorite jdk environment up and running. In my cas I used a container:

    docker run -it --rm -v $PWD:/UYBHYS2022formindChallenge arm64v8/openjdk /bin/sh
    sh-4.4# cd /UYBHYS2022formindChallenge
    sh-4.4# javac -d . SecurePassword.java

## Howto run the SecurePassword generator ?

Get you favorite jdk environment up and running. In my cas I used a container:

    docker run -it --rm -v $PWD:/UYBHYS2022formindChallenge arm64v8/openjdk /bin/sh
    sh-4.4# cd /UYBHYS2022formindChallenge
    sh-4.4# java com/formind/SecurePassword
    7e81SZRz0L2N
    7S7u0XjG6W9V
    PoS1n7A4EqbM

## How to build the hack ?

Get you favorite jdk environment up and running. In my cas I used a container:

    docker run -it --rm -v $PWD:/UYBHYS2022formindChallenge arm64v8/openjdk /bin/sh
    sh-4.4# cd /UYBHYS2022formindChallenge
    sh-4.4# javac -d . BrokenPassword.java

## Howto run the hack ?

Get you favorite jdk environment up and running. In my cas I used a container:

    docker run -it --rm -v $PWD:/UYBHYS2022formindChallenge arm64v8/openjdk /bin/sh
    sh-4.4# cd /UYBHYS2022formindChallenge
    sh-4.4# java com/formind/BrokenPassword
    Trying to hack the random generator with 7EHPFygG2gq2 and 7M7y9YTHt800
    Trying with 447603982 and 994447658
    Can't find the seed
    Trying with -447603982 and 994447658
    Found a seed (-29334174540630)
    and the second password matches ! (7M7y9YTHt800)
    The third password is: 

*The answer has been removed so you can have some fun with java*