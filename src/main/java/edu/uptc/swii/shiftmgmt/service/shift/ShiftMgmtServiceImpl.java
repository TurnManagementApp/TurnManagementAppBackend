package edu.uptc.swii.shiftmgmt.service.shift;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.swii.shiftmgmt.domain.model.Shift;
import edu.uptc.swii.shiftmgmt.domain.repository.ShiftRepository;

@Service
public class ShiftMgmtServiceImpl implements ShiftMgmtService {

    @Autowired
    ShiftRepository shiftRepository;

    @Override
    public void saveShift(Shift shift) {
        shiftRepository.save(shift);
    }

    @Override
    public List<Shift> listAllShifts() {
        return shiftRepository.findAll();
    }

    @Override
    public Optional<Shift> findById(Integer id) {
        return shiftRepository.findById(id);
    }

    @Override
    public String deleteTurn(Integer id) {
        Optional<Shift> toEliminate = shiftRepository.findById(id);
        if (!toEliminate.isEmpty()) {
            shiftRepository.delete(toEliminate.get());
            return "Shift Deleted";
        } else {
            return "Shift Not Found";
        }
    }

}
