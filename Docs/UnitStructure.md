# Unit Structure
A unit in army builder was (probably) originally a fairly simple thing, and _usually_ still is.
Unfortunately there is basically an exception to almost every rule, and the structure has clearly been adapted to handle these.

## Elements
The major elements of a unit are (in order): Groups, Profiles, Options, Items. All of these have an ID field, with numbers starting from 1 that may not be consecutive.
### Groups
Groups separate distinct combatants that are part of the same unit that are taken as a set, e.g. peacemakers and their auxbots, or manned tags and their pilot.
Generally, you purchase something from group #1 and that will give you the attendant extra by means of the `include` field in the option.
#### Exceptions
There are a couple of units that reference group 0. This refers to the top level unit object rather than a group, and will use the `options` set of the unit.
These options do not provide a combatant themselves but use the includes to provide the members of the unit. *Jazz and Billie* and *Scarface and Cordelia* use this approach.

Also the Aleph posthumans use groups to distinguish between the different marks of proxy.  

### Profiles
A profile is a statline of a model. It's the bit along the top of the model definition in army builder. 
Most models only have one profile, but some have two. These are basically used for units with the transmutation skill. 

The difference between a unit with one group with multiple profiles and one with multiple groups (each with a single profile) is that the former will only ever have one model on the board, it's just its stats change.

### Options
These are the different loadouts availiable to a model. This corresponds to the multiple lines to choose between under the profile in builder.

### Items
Skills, Equipment and Weapons are all treated basically the same in the backend. Both Profiles and Options have lists of each associated with a given selection, and the resultant model will have the union of both.

