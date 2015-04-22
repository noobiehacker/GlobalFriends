package co.mitoo.sashimi.models.jsonPojo.recieve;

import android.os.Bundle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import co.mitoo.sashimi.utils.StaticString;

/**
 * Created by david on 15-03-29.
 */
@JsonRootName(value = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRecieve {

    private String mitoo_action ;
    private String obj_id ;
    private String obj_type;

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }

    public String getObj_type() {
        return obj_type;
    }

    public void setObj_type(String obj_type) {
        this.obj_type = obj_type;
    }

    public String getMitoo_action() {
        return mitoo_action;
    }

    public void setMitoo_action(String mitoo_action) {
        this.mitoo_action = mitoo_action;
    }

    public NotificationRecieve(Bundle bundle) {
        setObj_id(bundle.getString(StaticString.notificationObjID));
        setObj_type(bundle.getString(StaticString.notificationObjType));
        setMitoo_action(bundle.getString(StaticString.notificationMitooAction));
    }

    public NotificationRecieve() {
    }
}
