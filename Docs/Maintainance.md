# Maintainance

So, as much as possible, I've avoided hard-coding things into this, however there are some points where it was impractical not to.

## Factions and Sectorals
The available factions and sectorals are listed in their resepective enums (`FACTION`, `SECTORAL`).
Slight care needs to be taken as the army API refers to both as factions. 
Also, for the purposes of this code base, the 'vanilla' option is considered a sectoral of the faction.

As new factions / sectorals are added / removed, these enums will need updating. 
Each entry has a number of bits of data associated with it, mostly of which should be obvious.
  
* fontTint - hex code of the colour for the faction / sectoral
* tint - json rgb code of the colour for the faction / sectoral
* hex - URL of the image to use on the bag hex

Note that sectorals can define these values to be `null`, in which case the parent faction value will be used.

## Model choice
In `PrintableUnit` there are a two lists - `invisbleWeapons` and `visibleEquipment`. These are used to determine whether two unit's models would be distinguishable.
If the mappings change (unlikely) or new weapons / equipment is added (more likely) then these may need altering. 

Also, at the bottom of PrintableUnit's constructor, there is the code to pick the 'distinguisher'. This is the bit of text after the model's name in the hover text.
It can probably do with tweaking algorithmically, but may also need updating as new things are added.
