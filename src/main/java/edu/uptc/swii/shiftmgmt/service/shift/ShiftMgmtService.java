package edu.uptc.swii.shiftmgmt.service.shift;

import java.util.List;

import edu.uptc.swii.shiftmgmt.domain.model.Shift;

public interface ShiftMgmtService {
    public void saveShift(Shift shift);

    public List<Shift> listAllShifts();

}
