package clientPackage.inputHandler;

import common.dataTransferObjects.CommandTransferObject;
import common.dataTransferObjects.CoordinatesTransferObject;
import common.dataTransferObjects.FlatTransferObject;
import common.dataTransferObjects.HouseTransferObject;

public class Validator {
    private static final long MAX_AREA = 897L;
    private static final long MIN_AREA = 0L;
    private static final Integer MAX_NUMBER_OF_ROOMS = 18;
    private static final Integer MIN_NUMBER_OF_ROOMS = 0;
    private static final double MIN_PRICE = 0d;
    private static final Integer MIN_LIVING_SPACE = 0;
    private static final Double MAX_X = 69D;
    private static final Integer MIN_YEAR = 0;
    private static final Integer MAX_NUMBER_OF_FLOORS = 20;
    private static final Integer MIN_NUMBER_OF_FLOORS = 0;
    private static final long MIN_NUMBER_OF_FLATS_ON_FLOOR = 0l;
    private static final Integer MIN_NUMBER_OF_LIFTS = 0;

    public boolean validate(CommandTransferObject command){
        return (command == null || (command.getCommand()!="" && command.getArgument()!=""));
    }
    public boolean validate(CoordinatesTransferObject coordinates){
        return coordinates!=null && coordinates.getX()!=null && coordinates.getX()<=MAX_X && coordinates.getY()!=null;
    }
    public boolean validate(FlatTransferObject flat){
        return  flat.getName()!=null &&
                !flat.getName().isEmpty() &&
                validate(flat.getCoordinates()) &&
                flat.getNumberOfRooms()!=null &&
                flat.getLivingSpace()!=null &&
                flat.getArea()>MIN_AREA &&
                flat.getArea()<=MAX_AREA &&
                flat.getNumberOfRooms() > MIN_NUMBER_OF_ROOMS &&
                flat.getNumberOfRooms() <= MAX_NUMBER_OF_ROOMS &&
                flat.getPrice() > MIN_PRICE &&
                flat.getLivingSpace() > MIN_LIVING_SPACE &&
                validate(flat.getHouse());
    }
    public boolean validate(HouseTransferObject house){
        return  (house==null) ||
                (house.getNumberOfFloors()!=null &&
                house.getNumberOfLifts()!=null &&
                (house.getYear()==null || house.getYear() > MIN_YEAR) &&
                house.getNumberOfFloors() <= MAX_NUMBER_OF_FLOORS &&
                house.getNumberOfFloors() > MIN_NUMBER_OF_FLOORS&
                house.getNumberOfFlatsOnFloor() > MIN_NUMBER_OF_FLATS_ON_FLOOR &&
                house.getNumberOfLifts() > MIN_NUMBER_OF_LIFTS);
    }
}
