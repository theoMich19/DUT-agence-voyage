package Outils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Class de convertion
 * 
 * @author Marco FRENDO, Théo MICHELLON, Dylan ROUX, Mathias MILHE-CASTEL
 */
public class Converteur {

  /**
   * Conversion Date to LocalDate
   * 
   * @param dateToConvert date à convertir
   * @return
   */
  public static LocalDate convertToLocalDate(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

  /**
   * Conversion LocalDate to Date
   * 
   * @param dateToConvert date à convertir
   * @return
   */
  public static Date convertToDate(LocalDate dateToConvert) {
    return java.util.Date.from(dateToConvert.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant());
  }
}
