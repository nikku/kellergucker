package de.nixis.kk.data;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class Status {

  private String status;


  public static Status created() {

    Status s = new Status();
    s.status = "created";

    return s;
  }


  public static Status jup() {

    Status s = new Status();
    s.status = "jup";

    return s;
  }

  public static Status removed() {

    Status s = new Status();
    s.status = "removed";

    return s;
  }

  public static Status error() {

    Status s = new Status();
    s.status = "error";

    return s;
  }
}
