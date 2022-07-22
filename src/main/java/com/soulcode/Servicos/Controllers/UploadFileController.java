package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Services.FuncionarioService;
import com.soulcode.Servicos.Util.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("servicos")

public class UploadFileController {

    @Autowired
    FuncionarioService funcionarioService;

    @PostMapping("/funcionarios/envioFoto/{idFuncionario}")
        public ResponseEntity<Void> enviarFoto(@PathVariable Integer idFuncionario, MultipartFile file, @RequestParam("nome") String nome) throws IOException {

        String fileName = nome;
        String uploadDir = "C:/Users/diogo/Desktop/fotos IJ/";
        String nomeMaisCaminho = uploadDir + nome;



        try {
            UploadFile.saveFile(uploadDir, fileName, file);
            funcionarioService.salvarFoto(idFuncionario, nomeMaisCaminho);
        }catch (IOException e){
            System.out.println("O arquivo nao foi enviado: " + e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
