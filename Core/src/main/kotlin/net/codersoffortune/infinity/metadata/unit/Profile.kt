package net.codersoffortune.infinity.metadata.unit

import net.codersoffortune.infinity.Util

data class Profile(
    // {"id":1,"arm":0,"ava":2,"bs":8,"bts":3,"cc":11,"chars":[2,27,5,21],"equip":[],"logo":"https://assets.corvusbelli.net/army/img/logo/units/yaozao-1-1.svg",
    // "weapons":[],"includes":[],"move":[15,10],"ph":10,"s":1,"str":true,"type":5,"w":1,"wip":13,"name":"YÁOZĂO","notes":null,
    // "skills":[{"extra":[41],"id":243,"order":1},{"id":84,"order":2},{"extra":[6],"id":28,"order":3},{"extra":[30],"id":162,"order":4}],"peripheral":[]}
    var id: Int = 0,
    var arm: Int = 0,
    var ava: Int = 0,
    var bs: Int = 0,
    var bts: Int = 0,
    var cc: Int = 0,
    var chars: List<Int>? = null,
    var equip // I THINK
    : List<ProfileItem>? = null,
    var logo: String? = null,
    var weapons: List<ProfileItem>? = null,
    var includes: List<ProfileInclude>? = null,
    var move: List<Int>? = null,
    var ph: Int = 0,
    var s: Int = 0,
    var isStr: Boolean = false,
    var type: Int = 0,
    var w: Int = 0,
    var wip: Int = 0,
    var name: String? = null,
    var notes: String? = null,
    var skills: List<ProfileItem>? = null,
    var peripheral: List<ProfileItem>? = null,
) {
    /**
     * Creates a _shallow_ copy with parameters changed to make this a foxhole version.
     */
    fun sapperCopy(): Profile {
        return Profile(
            id+Util.sapperProfileOffset,
            arm,
            ava,
            bs,
            bts,
            cc,
            chars,
            equip,
            logo,
            weapons,
            includes,
            move,
            ph,
            if (s<3) 3 else s,
            isStr,
            type,
            w,
            wip,
            name,
            notes,
            skills,
            peripheral
        )
    }
}