package pe.edu.cibertec.patitas_backend_a.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.patitas_backend_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_a.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_a.service.AutenticacionService;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public String[] validarUsuario(LoginRequestDTO loginRequestDTO) throws IOException {

        String[] datosUsuario = null;
        Resource resource = resourceLoader.getResource("classpath:usuarios.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))){

            String linea;
            while ((linea = br.readLine()) != null){

                String[] datos = linea.split(";");

                if (loginRequestDTO.tipoDocumento().equals(datos[0]) &&
                        loginRequestDTO.numeroDocumento().equals(datos[1]) &&
                        loginRequestDTO.password().equals(datos[2])) {

                    datosUsuario = new String[2];
                    datosUsuario[0] = datos[3]; // Recuperar nombre
                    datosUsuario[1] = datos[4]; // Recuperar correo
                    break;

                }

            }

        } catch (IOException e){

            datosUsuario = null;
            throw new IOException(e);

        }

        return datosUsuario;
    }

    @Override
    public void registroLogout(LogoutRequestDTO logoutRequestDTO) throws IOException {

        String path = "src/main/resources/sesionescerradas.txt";

        try (BufferedWriter br = new BufferedWriter(new FileWriter(path, true))) {
            br.write(logoutRequestDTO.tipoDocumento() + ";" + logoutRequestDTO.numeroDocumento() + ";" + LocalDate.now() + ";" + LocalTime.now() + "\n");

        } catch (IOException e) {
            throw new IOException("Error al registrar en el txt 'sesionescerradas' ", e);

        }

    }

}