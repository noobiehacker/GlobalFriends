package co.mitoo.sashimi.models.jsonPojo.recieve;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by david on 15-03-29.
 */
@JsonRootName(value = "")
public class JsonDeviceInfo {

    public String token;
    public String platform;
    public String model;
    public String app_version;
    public String os_version;
}
