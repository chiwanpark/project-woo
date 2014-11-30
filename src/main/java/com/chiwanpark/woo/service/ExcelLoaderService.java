package com.chiwanpark.woo.service;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesDatum;
import com.chiwanpark.woo.model.Tuple3;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;

@Service
public class ExcelLoaderService {
  private static final Logger LOG = LoggerFactory.getLogger(ExcelLoaderService.class);

  public Observation loadExcelFile(File file) throws IOException, InvalidFormatException {
    Workbook workbook = WorkbookFactory.create(file);
    Sheet sheet = workbook.getSheetAt(0);

    Observation observation = new Observation();

    observation.setName(getObservationName(sheet));
    observation.setType(getObservationType(sheet));
    observation.setHeight(getObservationHeight(sheet));
    observation.setLatitude(getObservationLatitude(sheet));
    observation.setLongitude(getObservationLongitude(sheet));

    for (int i = 7, lastRow = sheet.getLastRowNum(); i <= lastRow; ++i) {
      Row row = sheet.getRow(i);

      Date date = row.getCell(3).getDateCellValue();

      observation.insertWaterLevel(new TimeSeriesDatum(date, row.getCell(4).getNumericCellValue()));
      observation.insertTemperature(new TimeSeriesDatum(date, row.getCell(5).getNumericCellValue()));
      observation.insertConductivity(new TimeSeriesDatum(date, row.getCell(6).getNumericCellValue()));
    }

    LOG.info("Data file loaded: " + file.getAbsolutePath());
    LOG.info("Count of records: " + String.valueOf(sheet.getLastRowNum() - 6) + " records");

    return observation;
  }

  private String getObservationName(Sheet sheet) {
    return sheet.getRow(0).getCell(1).getStringCellValue();
  }

  private String getObservationType(Sheet sheet) {
    return sheet.getRow(1).getCell(1).getStringCellValue();
  }

  private double getObservationHeight(Sheet sheet) {
    return sheet.getRow(2).getCell(1).getNumericCellValue();
  }

  private Tuple3<Integer, Integer, Integer> getObservationLatitude(Sheet sheet) {
    return parsePosition(sheet.getRow(3).getCell(1).getStringCellValue());
  }

  private Tuple3<Integer, Integer, Integer> getObservationLongitude(Sheet sheet) {
    return parsePosition(sheet.getRow(4).getCell(1).getStringCellValue());
  }

  private Tuple3<Integer, Integer, Integer> parsePosition(String s) {
    Matcher matcher = Config.POSITION_PATTERN.matcher(s);
    if (matcher.find()) {
      return new Tuple3<>(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3)));
    }

    LOG.warn("Cannot parse position data!");
    return null;
  }
}
