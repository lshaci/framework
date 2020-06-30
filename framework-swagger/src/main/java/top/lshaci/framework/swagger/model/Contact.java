package top.lshaci.framework.swagger.model;

import lombok.Data;

/**
 * Contact information
 *
 * @author lshaci
 * @since 1.0.7
 */
@Data
public class Contact {
    /**
     * name
     */
    private String name = "lshaci";
    /**
     * url
     */
    private String url = "http://www.lshaci.top";
    /**
     * email
     */
    private String email = "lshaci@qq.com";

    public springfox.documentation.service.Contact get() {
        return new springfox.documentation.service.Contact(name, url, email);
    }
}
