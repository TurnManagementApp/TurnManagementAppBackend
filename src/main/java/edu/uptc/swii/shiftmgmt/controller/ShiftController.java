package edu.uptc.swii.shiftmgmt.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swii.shiftmgmt.domain.model.Shift;
import edu.uptc.swii.shiftmgmt.service.shift.ShiftMgmtService;
// import edu.uptc.swii.shiftmgmt.util.SendRequest;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    @Autowired
    private ShiftMgmtService shiftMgmtService;

    // private SendRequest sendRequest = new SendRequest();

    @PreAuthorize("hasRole('Users-client-role') or hasRole('Administrators-client-role')")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public String createShift(@RequestBody Shift shift) {
        shiftMgmtService.saveShift(shift);
        String json = "{ \"shiftlogs_table_name\":\"Shifts\", \"shiftlogs_action\":\"Creating Shifts\" }";
        // sendRequest.sendPostRequest(json);
        return " Shift Date: " + (shift.getShift_date()) + " Shift UserID " + (shift.getShift_user_id());
    }

    @PreAuthorize("hasRole('Administrators-client-role')")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET, produces = "application/json")
    public List<Shift> listShifts() {
        String json = "{ \"shiftlogs_table_name\":\"Shifts\", \"shiftlogs_action\":\"Searching Shifts\" }";
        // sendRequest.sendPostRequest(json);
        return shiftMgmtService.listAllShifts();
    }

    @PreAuthorize("hasRole('Users-client-role') or hasRole('Administrators-client-role')")
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.POST, produces = "application/json")
    public Optional<Shift> findById(@PathVariable Integer id) {
        return shiftMgmtService.findById(id);
    }

    @PreAuthorize("hasRole('Users-client-role') or hasRole('Administrators-client-role')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public String deleteTurn(@PathVariable Integer id) {
        // sendRequest.sendPostRequest("{\"shiftlogs_table_name\":\"Shifts\",
        // \"turnslogs_action\":\"Se ha eliminado un Turno de usuario\" }");
        return shiftMgmtService.deleteTurn(id);
    }

}
