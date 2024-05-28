package edu.uptc.swii.shiftmgmt.service.shift;

import java.util.List;
import java.util.Optional;

import edu.uptc.swii.shiftmgmt.domain.model.Shift;

public interface ShiftMgmtService {
    public void saveShift(Shift shift);

    public List<Shift> listAllShifts();

    public Optional<Shift> findById(Integer id);

    public String deleteTurn(Integer id);
}
