/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.eurofleets.ears3.dto;

import java.time.OffsetDateTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author thomas
 */
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

    public OffsetDateTime unmarshal(String v) throws Exception {
        return OffsetDateTime.parse(v);
    }

    public String marshal(OffsetDateTime v) throws Exception {
        return v.toString();
    }

}
