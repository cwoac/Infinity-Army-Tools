package net.codersoffortune.infinity.metadata;

import java.util.List;

public class Fireteam {
    // "conditions": [],
    //            "min": 1,
    //            "fireteams": [
    //                6,
    //                7,
    //                9,
    //                10
    //            ],
    //            "units": [
    //                {
    //                    "unit": 24,
    //                    "min": 1
    //                }
    //            ],
    //            "description": "Special Fireteam: Up to 1 Order Sergeant can join a Knights Hospitallers or a Teuton\n  Knights Fireteam. Except Crusade Fireteams.",
    //            "id": 8,
    //            "type": "WILDCARD",
    //            "order": 10
    private List<String> conditions;
    private int min;
    private List<Integer> fireteams;
    private List<FireteamUnit> units;
    private String description;
    private int id;
    private String type;
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FireteamUnit> getUnits() {
        return units;
    }

    public void setUnits(List<FireteamUnit> units) {
        this.units = units;
    }

    public List<Integer> getFireteams() {
        return fireteams;
    }

    public void setFireteams(List<Integer> fireteams) {
        this.fireteams = fireteams;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}
