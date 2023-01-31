package by.vitstep.organizer.web;

import by.vitstep.organizer.model.dto.CreateTxRequestDto;
import by.vitstep.organizer.model.dto.TxDto;
import by.vitstep.organizer.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/tx")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class TransactionController {
    TransactionService transactionService;
    @GetMapping("/get")
    public ResponseEntity<TxDto> get(@RequestParam @Min(value=1) Long id){
        return ResponseEntity.ok(transactionService.getTx(id));
    }
    @RolesAllowed("ROLE_USER")
    @PostMapping("/create")
    public  ResponseEntity<TxDto> create(@RequestBody @Valid CreateTxRequestDto request){
        return ResponseEntity.ok(transactionService.doTransact(request));
    }
}
