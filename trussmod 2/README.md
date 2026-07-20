# Stage Truss Mod (Forge 1.20.1)

Twee blokken voor stage rigging:

- **Tower Truss**: 1,5 x 1,5 blok breed en 3 blokken hoog. Je plaatst 1 item en de volledige toren verschijnt. Stapelbaar. Eender welk deel breken verwijdert de hele toren en dropt 1 item.
- **Speaker Bump**: headblock met uitkragende beams en 4 kettingtakels om een line array aan te hangen. Kan enkel bovenop een Tower Truss.

Beide zitten in de creative tab "Stage Truss".

## De jar laten bouwen via GitHub (geen Java nodig op je Mac)

1. Maak een gratis account op github.com als je er nog geen hebt.
2. Maak een nieuwe repository (de + rechtsboven > New repository). Geef ze een naam, bv. trussmod, en klik Create.
3. Upload alle bestanden uit deze map naar de repository (zie stappen in de chat).
4. GitHub bouwt de mod automatisch. Ga naar het tabblad "Actions", klik op de laatste build, en download onderaan bij "Artifacts" het bestand trussmod-jar.
5. Pak dat zip-je uit: daarin zit trussmod-1.0.0.jar. Dat is je mod.

De jar plaats je in de mods-map van je Forge 1.20.1 installatie.

## Lokaal bouwen (alternatief, vereist Java 17)

```
./gradlew build
```

Resultaat: build/libs/trussmod-1.0.0.jar
