package com.nsy.club.controller;

import com.nsy.club.dto.NoteDTO;
import com.nsy.club.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    /**
     * Note등록
     * @param noteDTO
     * @return
     */
    @PostMapping(value="")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO){

        log.info("-------------------register----------------");
        log.info(noteDTO);
        log.info("-------------------------------------------");

        Long num = noteService.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);

    }

    /**
     * Note 조회
     * @param num
     * @return
     */
    @GetMapping(value="/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num){
        log.info("-------------------read----------------");
        log.info(num);
        log.info("---------------------------------------");

        return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
    }

    /**
     * 특정 아이디의 note 모두 조회
     * @param email
     * @return
     */
    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email){
        log.info("-------------------getList-----------------");
        log.info(email);
        log.info("-------------------------------------------");

        return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);

    }

    /**
     * note 삭제
     * @param num
     * @return
     */
    @DeleteMapping(value="/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num") Long num){
        log.info("-------------------remove----------------");
        log.info(num);
        log.info("-----------------------------------------");

        noteService.remove(num);

        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    /**
     * note 수정
     * @param noteDTO
     * @return
     */
    @PutMapping(value="/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO){
        log.info("-------------------modify---------------------");
        log.info(noteDTO);

        noteService.modify(noteDTO);

        return new ResponseEntity<>("modified", HttpStatus.OK);

    }
}
