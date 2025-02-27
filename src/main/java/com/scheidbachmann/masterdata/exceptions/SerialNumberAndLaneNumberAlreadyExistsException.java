/**
 * Created By Amine Barguellil
 * Date : 2/29/2024
 * Time : 4:04 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.exceptions;

import com.scheidbachmann.masterdata.entity.Sensor;
import com.scheidbachmann.masterdata.enums.DirectionEnum;
import com.scheidbachmann.masterdata.repository.SensorRepository;
import com.scheidbachmann.masterdata.service.SensorsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class SerialNumberAndLaneNumberAlreadyExistsException extends RuntimeException {

    protected  String message;
    protected final String serialNumber;
    protected final String laneNumber;

    protected final String id;

    protected final SensorsService sensorsService;

//    protected final DirectionEnum directionEnum;

    protected final SensorRepository sensorRepository;



    protected SerialNumberAndLaneNumberAlreadyExistsException(final String serialNumber, final String laneNumber , String id, SensorsService sensorsService , SensorRepository sensorRepository) {
        this.serialNumber = requireNonNull(serialNumber, "The serialNumber must not be null!");
        this.laneNumber = requireNonNull(laneNumber, "The laneNumber must not be null!");
        this.id = id;
        this.sensorsService = sensorsService;
//        this.directionEnum = directionEnum;
        this.sensorRepository = sensorRepository;

        final Optional<Sensor> existingSensor =
                this.sensorRepository.findById(id);


        List<String> serialNumbers = sensorsService.getSerialNumbers();
        serialNumbers.remove(existingSensor.map(Sensor::getSerialNumber).orElse(null));

        if (serialNumbers.contains(serialNumber)) {
            this.message = String.format("The sensor with serial number '%s' already exists!", serialNumber);
            System.out.print(  "////////////" + this.message + "///////////");
        }
    }

    public static SerialNumberAndLaneNumberAlreadyExistsException with(final String serialNumber, final String laneNumber , final SensorRepository sensorRepository1  ,final String id , final SensorsService service ) {
        return new SerialNumberAndLaneNumberAlreadyExistsException(serialNumber, laneNumber, id, service,sensorRepository1);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getLaneNumber() {
        return laneNumber;
    }

}
