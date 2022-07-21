package com.nsy.club.service;

import com.nsy.club.dto.NoteDTO;
import com.nsy.club.entity.ClubMember;
import com.nsy.club.entity.ClubMemberRole;
import com.nsy.club.entity.Note;

import java.util.List;

public interface NoteService {

    /**
     * 등록
     * @param noteDTO
     * @return
     */
    Long register(NoteDTO noteDTO);

    /**
     * 조회
     * @param num
     * @return
     */
    NoteDTO get(Long num);

    /**
     * 수정
     * @param noteDTO
     */
    void modify(NoteDTO noteDTO);

    /**
     * 삭제
     * @param num
     */
    void remove(Long num);

    /**
     * 목록 조회
     * @param writerEmail
     * @return
     */
    List<NoteDTO> getAllWithWriter(String writerEmail);

    /**
     * dto를 entity로 변환
     * @param noteDTO
     * @return
     */
    default Note dtoToEntity(NoteDTO noteDTO){

        Note note = Note.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(ClubMember.builder().email(noteDTO.getWriterEmail()).build())
                .build();

        return note;
    }

    /**
     * entity를 dto로 변환
     * @param note
     * @return
     */
    default  NoteDTO entityToDTO(Note note){
        NoteDTO noteDTO = NoteDTO.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();

        return noteDTO;
    }
}
