package com.soltelec.consolaentrada.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import com.soltelec.consolaentrada.configuration.Conexion;
import com.soltelec.consolaentrada.models.Dtos.InfoHojaPruebas;
import com.soltelec.consolaentrada.models.entities.HojaPruebas;
import com.soltelec.consolaentrada.models.entities.Prueba;

public class Utils {
    
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    // Método para contar registros en la tabla hoja_pruebas según el valor de con_hoja_prueba
    public static int contarRegistrosHojaPruebas(int conHojaPrueba) {
        int nHp = 0;  // Inicializamos el contador en 0
        Conexion.setConexionFromFile();

        // Consulta SQL
        String query = "SELECT COUNT(*) AS nHp FROM hoja_pruebas WHERE con_hoja_prueba = ?";

        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
            PreparedStatement stmt = conexion.prepareStatement(query)) {

            // Asigna el valor al parámetro
            stmt.setInt(1, conHojaPrueba);

            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nHp = rs.getInt("nHp");  // Obtiene el valor del conteo
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nHp;
    }

    public static String obtenerFechaPrimeraRevision(int conHojaPrueba, Integer testSheet) {
        String fechaIngreso = null;  // Inicializamos la fecha como null
        Conexion.setConexionFromFile();
    
        // Consulta SQL
        String query = "SELECT Fecha_ingreso_vehiculo AS fecha_ingreso " +
                    "FROM hoja_pruebas " +
                    "WHERE con_hoja_prueba = ? AND TESTSHEET <> ?";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
            PreparedStatement stmt = conexion.prepareStatement(query)) {
    
            // Asigna los valores a los parámetros
            stmt.setInt(1, conHojaPrueba);
            stmt.setInt(2, testSheet);
    
            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp fecha = rs.getTimestamp("fecha_ingreso");  // Obtiene el valor de fecha y hora
    
                // Formatea la fecha y hora a String en el formato deseado
                if (fecha != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    fechaIngreso = dateFormat.format(fecha);
                }
            }
    
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return fechaIngreso;
    }

    public static int contarHojaPrueba(int conHojaPrueba) {
        int count = 0;  // Inicializamos el contador como 0
        Conexion.setConexionFromFile();
        
        // Consulta SQL
        String query = "SELECT COUNT(*) AS total " +
                       "FROM hoja_pruebas " +
                       "WHERE con_hoja_prueba = ?";
        
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
        
            // Asigna el valor al parámetro
            stmt.setInt(1, conHojaPrueba);
        
            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");  // Obtiene el valor del conteo
            }
        
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    public static Integer guardarHojaPruebas(HojaPruebas hojaPruebasReinspeccion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            // Establecer la conexión
            String url = Conexion.getUrl(); 
            String user = Conexion.getUsuario(); 
            String password = Conexion.getContrasena();

            connection = DriverManager.getConnection(url, user, password);

            // Verificar y agregar columnas faltantes si es necesario
            ensureColumnsExist(connection);

            // Preparar la sentencia SQL para insertar
            String sql = "INSERT INTO hoja_pruebas (Vehiculo_for, Propietario_for, Usuario_for, Hoja_activa_activeflag, Finalizada, estado, Impreso, "
                    + "Fecha_ingreso_vehiculo, Anulado, Aprobado, Fecha_expiracion_revision, Conductor, Consecutivo_resolucion, Cerrada, "
                    + "Fecha_expedicion_certificados, Comentarios_cda, Numero_intentos, id_fotos_for, consecutivo_runt, "
                    + "numero_solicitud, usuario_resp, preventiva, con_hoja_prueba, pin, estado_sicov, "
                    + "forma_med_temp, fk_aseguradora, fecha_exp_soat, fecha_venc_soat, "
                    + "nro_soat, Ubicacion_municipio, kilometraje_rtm, fecha_venc_gnv, MetodoMedicionRpm) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'V')";

            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // Asignar valores a la sentencia
            preparedStatement.setInt(1, hojaPruebasReinspeccion.getVehiculo().getId());
            preparedStatement.setLong(2, hojaPruebasReinspeccion.getPropietario().getId());
            preparedStatement.setInt(3, hojaPruebasReinspeccion.getUsuario());
            preparedStatement.setString(4, hojaPruebasReinspeccion.getActiva());
            preparedStatement.setString(5, hojaPruebasReinspeccion.getFinalizada());
            preparedStatement.setString(6, hojaPruebasReinspeccion.getEstado());
            preparedStatement.setString(7, hojaPruebasReinspeccion.getImpreso());

            /* if (new java.sql.Timestamp(hojaPruebasReinspeccion.getFechaIngreso().getTime()) != null) {
                System.out.println("Fecha ingreso (Timestamp): " + new java.sql.Timestamp(hojaPruebasReinspeccion.getFechaIngreso().getTime()));
                return -1;
            } */
            preparedStatement.setTimestamp(8, new java.sql.Timestamp(hojaPruebasReinspeccion.getFechaIngreso().getTime()));
            preparedStatement.setString(9, hojaPruebasReinspeccion.getAnulado());
            preparedStatement.setString(10, hojaPruebasReinspeccion.getAprobado());
            if (hojaPruebasReinspeccion.getFechaExpiracion() != null) {
                preparedStatement.setDate(11, new java.sql.Date(hojaPruebasReinspeccion.getFechaExpiracion().getTime()));
            } else {
                preparedStatement.setNull(11, java.sql.Types.DATE);
            }
            preparedStatement.setLong(12, hojaPruebasReinspeccion.getConductor().getId());
            preparedStatement.setString(13, hojaPruebasReinspeccion.getConsecutivo());
            preparedStatement.setString(14, hojaPruebasReinspeccion.getCerrada());
            preparedStatement.setDate(15, new java.sql.Date(hojaPruebasReinspeccion.getFechaExpedicion().getTime()));
            preparedStatement.setString(16, hojaPruebasReinspeccion.getComentario());
            preparedStatement.setInt(17, hojaPruebasReinspeccion.getIntentos());
            preparedStatement.setInt(18, hojaPruebasReinspeccion.getNroPruebasRegistradas());
            preparedStatement.setString(19, hojaPruebasReinspeccion.getConsecutivoRunt());
            preparedStatement.setString(20, hojaPruebasReinspeccion.getNumeroSolicitud());
            preparedStatement.setInt(21, hojaPruebasReinspeccion.getResponsable().getUsuario());
            preparedStatement.setString(22, hojaPruebasReinspeccion.getPreventiva());
            preparedStatement.setInt(23, hojaPruebasReinspeccion.getCon_hoja_prueba());
            preparedStatement.setString(24, hojaPruebasReinspeccion.getPin());
            preparedStatement.setString(25, hojaPruebasReinspeccion.getEstadoSICOV());
            preparedStatement.setString(26,  String.valueOf(hojaPruebasReinspeccion.getFormaMedTemperatura()));
            preparedStatement.setInt(27, hojaPruebasReinspeccion.getAseguradora().getId());
            preparedStatement.setDate(28, new java.sql.Date(hojaPruebasReinspeccion.getFechaExpSoat().getTime()));
            preparedStatement.setDate(29, new java.sql.Date(hojaPruebasReinspeccion.getFechaVencSoat().getTime()));
            preparedStatement.setString(30, hojaPruebasReinspeccion.getNroIdentificacionSoat());
            preparedStatement.setString(31, hojaPruebasReinspeccion.getUbicacionMunicipio());
            preparedStatement.setString(32, hojaPruebasReinspeccion.getKilometraje());
            java.util.Date fechaVencimientoGnv = hojaPruebasReinspeccion.getFechaVencimientoGnv();
            if (fechaVencimientoGnv != null) {
                preparedStatement.setDate(33, new java.sql.Date(fechaVencimientoGnv.getTime()));
            } else {
                preparedStatement.setNull(33, java.sql.Types.DATE);
            }

            // Ejecutar la inserción
            int rowsInserted = preparedStatement.executeUpdate(); //linea 189
            if (rowsInserted > 0) {
                System.out.println("Una nueva hoja de pruebas fue insertada exitosamente.");
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Integer idGenerado = generatedKeys.getInt(1);
                    hojaPruebasReinspeccion.setId(idGenerado);
                    return idGenerado;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo replicar la hoja de pruebas");
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    private static void ensureColumnsExist(Connection connection) throws SQLException {
        Set<String> existingColumns = new HashSet<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM hoja_pruebas");

        while (rs.next()) {
            existingColumns.add(rs.getString("Field"));
        }

        // Mapeo de columnas con sus tipos y valores predeterminados
        Map<String, String> requiredColumns = new HashMap<>();
        requiredColumns.put("Vehiculo_for", "int NOT NULL");
        requiredColumns.put("Propietario_for", "bigint NOT NULL");
        requiredColumns.put("Usuario_for", "int NOT NULL");
        requiredColumns.put("Hoja_activa_activeflag", "varchar(1) DEFAULT NULL");
        requiredColumns.put("Finalizada", "varchar(1) NOT NULL");
        requiredColumns.put("estado", "varchar(50) DEFAULT NULL");
        requiredColumns.put("Impreso", "varchar(1) DEFAULT NULL");
        requiredColumns.put("Fecha_ingreso_vehiculo", "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP");
        requiredColumns.put("Anulado", "varchar(1) DEFAULT NULL");
        requiredColumns.put("Aprobado", "varchar(1) DEFAULT NULL");
        requiredColumns.put("Fecha_expiracion_revision", "timestamp NULL DEFAULT NULL");
        requiredColumns.put("Conductor", "bigint NOT NULL");
        requiredColumns.put("Consecutivo_resolucion", "varchar(10) DEFAULT NULL");
        requiredColumns.put("Cerrada", "varchar(1) DEFAULT NULL");
        requiredColumns.put("Fecha_expedicion_certificados", "timestamp NULL DEFAULT NULL");
        requiredColumns.put("Comentarios_cda", "mediumtext");
        requiredColumns.put("Numero_intentos", "int NOT NULL");
        requiredColumns.put("id_fotos_for", "int DEFAULT NULL");
        requiredColumns.put("consecutivo_runt", "varchar(40) DEFAULT NULL");
        requiredColumns.put("numero_solicitud", "varchar(45) DEFAULT NULL");
        requiredColumns.put("usuario_resp", "int DEFAULT NULL");
        requiredColumns.put("preventiva", "varchar(1) NOT NULL DEFAULT 'N'");
        requiredColumns.put("con_hoja_prueba", "int DEFAULT NULL");
        requiredColumns.put("pin", "varchar(200) DEFAULT NULL");
        requiredColumns.put("estado_sicov", "enum('PENDIENTE','SINCRONIZADO','FINALIZADO','FALLIDO','INICIADO','NO_APLICA','Env1FUR') DEFAULT 'PENDIENTE'");
        requiredColumns.put("forma_med_temp", "char(1) DEFAULT NULL COMMENT 'TIPIFICA COMO SE MIDIO LA TEMPERATURA'");
        requiredColumns.put("fk_aseguradora", "int DEFAULT NULL COMMENT 'fk que vincula a la tabla aseguradora'");
        requiredColumns.put("fecha_exp_soat", "datetime DEFAULT NULL");
        requiredColumns.put("fecha_venc_soat", "datetime DEFAULT NULL");
        requiredColumns.put("nro_soat", "varchar(200) DEFAULT NULL");
        requiredColumns.put("kilometraje_rtm", "varchar(50) NOT NULL DEFAULT '0'");
        requiredColumns.put("fecha_venc_gnv", "date DEFAULT '0000-00-00'");
        requiredColumns.put("MetodoMedicionRpm", "varchar(5) NOT NULL DEFAULT 'V'");

        // Agregar columnas que no existen
        for (Map.Entry<String, String> column : requiredColumns.entrySet()) {
            if (!existingColumns.contains(column.getKey())) {
                try {
                    stmt.executeUpdate("ALTER TABLE hoja_pruebas ADD COLUMN `" + column.getKey() + "` " + column.getValue());
                    System.out.println("Columna " + column.getKey() + " creada.");
                } catch (SQLException e) {
                    System.err.println("Error al crear la columna " + column.getKey() + ": " + e.getMessage());
                }
            }
        }

        rs.close();
        stmt.close();
    }

    public static int guardarPrueba(Prueba copiaPruebas, int idNuevaHojaPrueba) {
        int nuevoIdPrueba = -1;  // Este será el ID de la nueva prueba creada

        // Consulta SQL para insertar la nueva prueba
        String insertQuery = "INSERT INTO pruebas (Fecha_prueba, Tipo_prueba_for, Fecha_final, hoja_pruebas_for, " +
                            "usuario_for, id_tipo_aborto, Autorizada, Aprobada, Finalizada, Abortada, Fecha_aborto, " +
                            "Comentario_aborto, serialEquipo, observaciones, Pista) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
            PreparedStatement insertStmt = conexion.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Asignación de parámetros para la inserción utilizando los valores de copiaPruebas
            insertStmt.setDate(1, new java.sql.Date(copiaPruebas.getFecha().getTime()));
            insertStmt.setInt(2, copiaPruebas.getTipoPrueba().getId());
            insertStmt.setDate(3, new java.sql.Date(copiaPruebas.getFechaFinal().getTime()));
            insertStmt.setInt(4, idNuevaHojaPrueba);  // Asignamos el nuevo valor de hoja_pruebas_for
            insertStmt.setObject(5, copiaPruebas.getUsuarioFor().getUsuario());
            insertStmt.setObject(6, copiaPruebas.getIdTipoAborto());
            insertStmt.setString(7, copiaPruebas.getAutorizada());
            insertStmt.setString(8, copiaPruebas.getAprobado());
            insertStmt.setString(9, copiaPruebas.getFinalizada());
            insertStmt.setString(10, copiaPruebas.getAbortado());
            insertStmt.setString(11, copiaPruebas.getFechaAborto());
            insertStmt.setString(12, copiaPruebas.getComentario());
            insertStmt.setString(13, copiaPruebas.getSerialEquipo());
            insertStmt.setString(14, copiaPruebas.getObservaciones());
            insertStmt.setObject(15, copiaPruebas.getPista());

            // Ejecuta la inserción
            int affectedRows = insertStmt.executeUpdate();

            // Si la inserción fue exitosa, obtiene el ID de la nueva prueba
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nuevoIdPrueba = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo replicar la prueba: "+ copiaPruebas.getTipoPrueba().getDescripcion());
        }

        return nuevoIdPrueba;
    }

    public static boolean replicarMedidas(int idPruebaVieja, int idPruebaNueva) {
        Conexion.setConexionFromFile();
    
        // Consulta SQL para obtener las medidas de la prueba vieja
        String selectQuery = "SELECT MEASURETYPE, Valor_medida, Condicion, Simult " +
                             "FROM medidas " +
                             "WHERE TEST = ?";
    
        // Consulta SQL para insertar una nueva medida con el nuevo idPruebaNueva
        String insertQuery = "INSERT INTO medidas (MEASURETYPE, Valor_medida, TEST, Condicion, Simult) " +
                             "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement selectStmt = conexion.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conexion.prepareStatement(insertQuery)) {
    
            // Asignación del idPruebaVieja al parámetro de la consulta de selección
            selectStmt.setInt(1, idPruebaVieja);
    
            // Ejecutar la consulta de selección
            ResultSet rs = selectStmt.executeQuery();
    
            // Recorrer las medidas obtenidas y duplicarlas
            while (rs.next()) {
                int measureType = rs.getInt("MEASURETYPE");
                float valorMedida = rs.getFloat("Valor_medida");
                String condicion = rs.getString("Condicion");
                String simult = rs.getString("Simult");
    
                // Asignar los valores para la inserción de la medida copiada
                insertStmt.setInt(1, measureType);
                insertStmt.setFloat(2, valorMedida);
                insertStmt.setInt(3, idPruebaNueva);  // Asignamos el nuevo idPruebaNueva
                insertStmt.setString(4, condicion);
                insertStmt.setString(5, simult);
    
                // Ejecutar la inserción de la medida duplicada
                insertStmt.executeUpdate();
            }
    
            // Cerrar ResultSet
            rs.close();

            return true;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean guardarDefxPrueba(int idPruebaVieja, int idPruebaNueva) {
        Conexion.setConexionFromFile();
    
        // Consulta SQL para obtener los defectos de la prueba vieja
        String selectQuery = "SELECT id_defecto, Tipo_Defecto, tercer_estado " +
                             "FROM defxprueba " +
                             "WHERE id_prueba = ?";
    
        // Consulta SQL para insertar un nuevo defecto con el nuevo idPruebaNueva
        String insertQuery = "INSERT INTO defxprueba (id_defecto, id_prueba, Tipo_Defecto, tercer_estado) " +
                             "VALUES (?, ?, ?, ?)";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement selectStmt = conexion.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conexion.prepareStatement(insertQuery)) {
    
            // Asignación del idPruebaVieja al parámetro de la consulta de selección
            selectStmt.setInt(1, idPruebaVieja);
    
            // Ejecutar la consulta de selección
            ResultSet rs = selectStmt.executeQuery();
    
            // Recorrer los defectos obtenidos y duplicarlos
            while (rs.next()) {
                int idDefecto = rs.getInt("id_defecto");
                String tipoDefecto = rs.getString("Tipo_Defecto");
                String tercerEstado = rs.getString("tercer_estado");
    
                // Asignar los valores para la inserción del defecto copiado
                insertStmt.setInt(1, idDefecto);
                insertStmt.setInt(2, idPruebaNueva);  // Asignamos el nuevo idPruebaNueva
                insertStmt.setString(3, tipoDefecto);
                insertStmt.setString(4, tercerEstado);
    
                // Ejecutar la inserción del defecto duplicado
                insertStmt.executeUpdate();
            }
    
            // Cerrar ResultSet
            rs.close();
            return true;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Date obtenerFechaAnterior(int hojaPruebasId) {
        Date fechaAnterior = null; // Inicializamos la variable de fecha
        
        Conexion.setConexionFromFile();
        
        // Consulta SQL
        String query = "SELECT fecha_anterior FROM reinspecciones WHERE hoja_pruebas = ?";
        
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
            
            // Asigna el valor al parámetro
            stmt.setInt(1, hojaPruebasId);
            
            // Ejecuta la consulta
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("fecha_anterior");
                if (timestamp != null) {
                    fechaAnterior = new Date(timestamp.getTime()); // Convertir Timestamp a Date
                }
            }
            
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return fechaAnterior; // Retorna la fecha encontrada o null si no hay resultado
    }

    public static void actualizarFechaIngresoVehiculo(int testSheetId) {
        
        // Consulta SQL para actualizar la fecha
        String query = "UPDATE hoja_pruebas SET Fecha_ingreso_vehiculo = ? WHERE TESTSHEET = ?";
        
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
            
            // Establece la fecha y hora actual
            Timestamp fechaActual = new Timestamp(new Date().getTime());
            stmt.setTimestamp(1, fechaActual); // Asigna el valor de la fecha actual
            stmt.setInt(2, testSheetId); // Asigna el ID de la hoja de prueba
            
            // Ejecuta la actualización
            int filasActualizadas = stmt.executeUpdate();
            System.out.println("Filas actualizadas: " + filasActualizadas); // Confirmación de actualización
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int obtenerNumeroRevisionFoto(int idHojaPruebasFor) {
        // Consulta SQL para obtener el número de revisión desde la tabla fotos
        String query = "SELECT numeroRevision FROM fotos WHERE id_hoja_pruebas_for = ?";
        
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
            
            // Asigna el valor del parámetro
            stmt.setInt(1, idHojaPruebasFor);
            
            // Ejecuta la consulta
            try (ResultSet resultSet = stmt.executeQuery()) {
                // Verifica si hay un resultado
                if (resultSet.next()) {
                    // Retorna el valor de la columna numeroRevision
                    return resultSet.getInt("numeroRevision");
                } else {
                    // Si no hay resultados, lanza una excepción o retorna un valor por defecto
                    throw new IllegalArgumentException("No se encontró un registro en la tabla 'fotos' para el ID especificado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar la base de datos.", e);
        }
    }

    public static Integer obtenerKilometraje(int numeroHojaPruebas, String orden) {
        // Valida el valor de orden para evitar inyecciones SQL
        if (!"ASC".equalsIgnoreCase(orden) && !"DESC".equalsIgnoreCase(orden)) {
            throw new IllegalArgumentException("El parámetro 'orden' solo puede ser 'ASC' o 'DESC'.");
        }
        
        // Consulta SQL con placeholder para el orden dinámico
        String query = "SELECT m.Valor_medida " +
                       "FROM hoja_pruebas hp " +
                       "INNER JOIN pruebas p ON p.hoja_pruebas_for = hp.TESTSHEET " +
                       "INNER JOIN medidas m ON m.TEST = p.Id_Pruebas " +
                       "WHERE m.MEASURETYPE = 1006 AND hp.TESTSHEET = ? " +
                       "ORDER BY m.MEASURE " + orden + " " +
                       "LIMIT 1";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
    
            // Asigna el valor de los parámetros
            stmt.setInt(1, numeroHojaPruebas);
    
            // Ejecuta la consulta
            try (ResultSet resultSet = stmt.executeQuery()) {
                // Verifica si hay un resultado
                if (resultSet.next()) {
                    // Retorna el valor de la columna Valor_medida
                    return resultSet.getInt("Valor_medida");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar la base de datos.", e);
        }
        return 0;
    }

    public static boolean verificarConsecutivoRunt(int numeroHojaPruebas) {
        // Consulta SQL para verificar si el campo consecutivo_runt no es nulo
        String query = "SELECT CASE WHEN hp.consecutivo_runt IS NOT NULL THEN 1 ELSE 0 END AS resultado " +
                       "FROM hoja_pruebas hp " +
                       "WHERE hp.TESTSHEET = ?";
        
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
    
            // Asigna el valor del parámetro
            stmt.setInt(1, numeroHojaPruebas);
    
            // Ejecuta la consulta
            try (ResultSet resultSet = stmt.executeQuery()) {
                // Verifica si hay un resultado
                if (resultSet.next()) {
                    // Retorna true si consecutivo_runt no es nulo, false en caso contrario
                    return resultSet.getInt("resultado") == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al consultar la base de datos.", e);
        }
    
        // Si no se encuentra el registro, se asume que es nulo
        return false;
    }

    public static void insertarConsecutivoRunt(int numeroHojaPruebas, String consecutivoRunt) {
        // Consulta SQL para actualizar el campo consecutivo_runt
        String query = "UPDATE hoja_pruebas " +
                       "SET consecutivo_runt = ? " +
                       "WHERE TESTSHEET = ?";
        
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
    
            // Asigna los valores de los parámetros
            stmt.setString(1, consecutivoRunt);
            stmt.setInt(2, numeroHojaPruebas);
    
            // Ejecuta la actualización
            int filasActualizadas = stmt.executeUpdate();
    
            // Verifica si se actualizó alguna fila
            if (filasActualizadas > 0) {
                System.out.println("El consecutivo_runt se insertó correctamente.");
            } else {
                System.out.println("No se encontró una hoja de pruebas con el número proporcionado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar el consecutivo_runt en la base de datos.", e);
        }
    }

    public static boolean actualizarFotoPorHojaPruebas(int idHojaPruebasOriginal, int nuevoIdHojaPruebas) {
        Connection connection = null;
        PreparedStatement updateStatement = null;
    
        try {
            // Establecer conexión
            String url = Conexion.getUrl();
            String user = Conexion.getUsuario();
            String password = Conexion.getContrasena();
            connection = DriverManager.getConnection(url, user, password);
    
            // Actualizar el campo id_hoja_pruebas_for en los registros de fotos asociados al idHojaPruebasOriginal
            String updateSql = "UPDATE fotos SET id_hoja_pruebas_for = ? WHERE id_hoja_pruebas_for = ?";
            updateStatement = connection.prepareStatement(updateSql);
    
            // Establecer los parámetros
            updateStatement.setInt(1, nuevoIdHojaPruebas);
            updateStatement.setInt(2, idHojaPruebasOriginal);
    
            // Ejecutar la actualización
            int rowsUpdated = updateStatement.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Se actualizaron " + rowsUpdated + " registros de fotos con el nuevo id_hoja_pruebas_for: " + nuevoIdHojaPruebas);
                return true;
            } else {
                System.out.println("No se encontraron registros de fotos con id_hoja_pruebas_for = " + idHojaPruebasOriginal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar las fotos");
        } finally {
            try {
                if (updateStatement != null) updateStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    

    public static String getAprobadoReprobado(int idHojaPrueba) {
        String consulta = "SELECT p.*, v.CARTYPE FROM pruebas p \n" +
                          "INNER JOIN hoja_pruebas hp on hp.TESTSHEET = p.hoja_pruebas_for\n" +
                          "inner join vehiculos v on v.CAR = hp.Vehiculo_for " +
                          "WHERE hp.TESTSHEET = ?\n" +
                          "ORDER BY p.Fecha_prueba DESC;";
        
        // Archivo donde se guardará el log
        File logFile = new File("pruebas_log.txt");
        
        try (PrintStream logStream = new PrintStream(new FileOutputStream(logFile))) {
            // Redirige System.out a logStream
            PrintStream originalOut = System.out;
            System.setOut(logStream);
            
            Conexion.setConexionFromFile();

            boolean[] pruebasVistas = {false, false, false, false, false, false, false, false, false};

            try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
                 PreparedStatement consultaPruebas = conexion.prepareStatement(consulta)) {

                consultaPruebas.setInt(1, idHojaPrueba);

                try (ResultSet rc = consultaPruebas.executeQuery()) {
                    int puntajeTotalDefectos = 0;
                    while (rc.next()) {

                        int tipoPrueba = rc.getInt("Tipo_prueba_for");

                        if (!pruebasVistas[tipoPrueba - 1]) {
                            // Este mensaje será redirigido al archivo
                            System.out.println("id prueba: " + rc.getString("id_Pruebas"));
                            
                            pruebasVistas[tipoPrueba - 1] = true;

                            String finalizada = rc.getString("Finalizada");
                            String abortada = rc.getString("Abortada");

                            if ("N".equals(finalizada) || !"N".equals(abortada)) {
                                System.setOut(originalOut); // Restaura System.out
                                return "PENDIENTE";
                            }

                            int idPrueba = rc.getInt("Id_Pruebas");
                            int tipoVehiculo = rc.getInt("CARTYPE");

                            puntajeTotalDefectos += calcularPuntajeDefectos(idPrueba);
                            System.out.println("puntuacion defectos: "+puntajeTotalDefectos);

                            if (puntajeTotalDefectos > 4 && tipoVehiculo == 4) {
                                System.setOut(originalOut); // Restaura System.out
                                return "REPROBADA";
                            }
                            if (puntajeTotalDefectos > 9 && tipoVehiculo != 4) {
                                System.setOut(originalOut); // Restaura System.out
                                return "REPROBADA";
                            }
                            
                        }
                        //CMensajes.mensajeCorrecto(tipoPrueba+": id: "+rc.getString("id_Pruebas")+"\n"+puntajeTotalDefectos);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(logStream); // También loguea las excepciones en el archivo
            } finally {
                System.setOut(originalOut); // Restaura System.out en caso de error o al final
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "APROBADA";
    }

    public static int calcularPuntajeDefectos(int idPrueba) {
        // Consulta SQL para obtener los tipos de defecto asociados a una prueba específica
        String consulta = "SELECT d.Tipo_defecto FROM defectos d " +
                          "INNER JOIN defxprueba dp ON dp.id_defecto = d.CARDEFAULT " +
                          "WHERE dp.id_prueba = ?";
    
        // Inicializar la conexión y variables necesarias
        Conexion.setConexionFromFile();
        int puntajeTotal = 0;
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement consultaDefectos = conexion.prepareStatement(consulta)) {
    
            // Establecer el parámetro de la consulta
            consultaDefectos.setInt(1, idPrueba);
    
            // Ejecutar la consulta
            try (ResultSet rs = consultaDefectos.executeQuery()) {
                while (rs.next()) {
                    // Obtener el tipo de defecto
                    String tipoDefecto = rs.getString("Tipo_defecto");
    
                    // Sumar el puntaje según el tipo de defecto
                    if ("A".equals(tipoDefecto)) puntajeTotal += 10;
                    
                    if ("B".equals(tipoDefecto)) puntajeTotal += 1;

                    

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Retornar el puntaje total calculado (0 si no se encontraron registros)
        return puntajeTotal;
    }

    public static Integer getIdUltimaPruebaPorTipo(int tipoPrueba, int hojaPruebas) {
        String consulta = "SELECT p.Id_Pruebas FROM pruebas p " +
                        "INNER JOIN hoja_pruebas hp ON hp.TESTSHEET = p.hoja_pruebas_for " +
                        "INNER JOIN vehiculos v ON v.CAR = hp.Vehiculo_for " +
                        "INNER JOIN medidas m ON m.TEST = p.Id_Pruebas "+
                        "WHERE hp.TESTSHEET = ? AND p.Tipo_prueba_for = ? AND p.serialEquipo IS NOT NULL AND p.Abortada = 'N' \n" +
                        "ORDER BY p.Fecha_prueba DESC " +
                        "LIMIT 1;";

        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
            PreparedStatement consultaPruebas = conexion.prepareStatement(consulta)) {

            // Asigna los parámetros de la consulta
            consultaPruebas.setInt(1, hojaPruebas);
            consultaPruebas.setInt(2, tipoPrueba);

            try (ResultSet rs = consultaPruebas.executeQuery()) {
                // Si hay un resultado, retornamos el Id_Pruebas
                if (rs.next()) {
                    return rs.getInt("Id_Pruebas");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si no se encuentra ningún resultado, retorna null
        return null;
    }

    public static boolean verificarExistenciaFurData() {
        String consultaColumnas = "SELECT COLUMN_NAME " +
                                  "FROM INFORMATION_SCHEMA.COLUMNS " +
                                  "WHERE TABLE_NAME = 'hoja_pruebas' AND TABLE_SCHEMA = '" + Conexion.getBaseDatos() + "' " +
                                  "AND COLUMN_NAME IN ('furData', 'logWrite')";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery(consultaColumnas)) {
    
            // Variables para verificar la existencia de las columnas
            boolean furDataExiste = false;
            boolean logWriteExiste = false;
    
            // Verificar si alguna de las columnas existe
            while (resultado.next()) {
                String columna = resultado.getString("COLUMN_NAME");
                if ("furData".equals(columna)) {
                    furDataExiste = true;
                }
                if ("logWrite".equals(columna)) {
                    logWriteExiste = true;
                }
            }
    
            // Retorna true si ambas columnas existen
            return furDataExiste && logWriteExiste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // Retorna false si alguna de las columnas no existe
    }

    public static boolean crearColumnasParaAlmacenamiendoPdfs() {
        // Consulta para verificar las columnas 'furData' y 'logWrite'
        
        boolean existeFurData = existeColumna("furData");
        boolean existeLogWrite = existeColumna("logWrite");

        if (existeLogWrite && existeFurData) return false;

        String alterTable = "ALTER TABLE hoja_pruebas ";
        // Añadir la columna 'furData' si no existe
        if (!existeFurData) {
            alterTable += "ADD COLUMN furData MEDIUMBLOB, ";
        }

        // Añadir la columna 'logWrite' si no existe
        if (!existeLogWrite) {
            alterTable += "ADD COLUMN logWrite VARCHAR(10000), "; // 65535 es un límite común para textos largos
        }

        // Eliminar la coma final si no se añadió ninguna columna
        if (alterTable.endsWith(", ")) {
            alterTable = alterTable.substring(0, alterTable.length() - 2);
        }

        if (!existeFurData || !existeLogWrite) {
            try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
                Statement stmt = conexion.createStatement()) {
    
                // Ejecutar la modificación de la tabla para agregar las columnas
                if (!alterTable.isEmpty()) {
                    stmt.executeUpdate(alterTable);
                    return true; // Las columnas fueron creadas exitosamente
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
            
    
        return false; // Las columnas ya existían o hubo un error
    }

    private static boolean existeColumna(String nombreColumna) {
        String consultaColumnas = "SELECT COLUMN_NAME " +
                                  "FROM INFORMATION_SCHEMA.COLUMNS " +
                                  "WHERE TABLE_NAME = 'hoja_pruebas' AND TABLE_SCHEMA = '" + Conexion.getBaseDatos() + "' " +
                                  "AND COLUMN_NAME = '" + nombreColumna + "'";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery(consultaColumnas)) {
    
            // Si se encuentra la columna, retorna true
            return resultado.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // La columna no existe
    }

    public static boolean actualizarPdfFur(int idHojaPruebas, byte[] furData, String logWrite) {
        // Consulta SQL para actualizar los valores en las columnas 'furData' y 'logWrite' basándose en el id
        String consultaUpdate = "UPDATE hoja_pruebas SET furData = ?, logWrite = ? WHERE TESTSHEET = ?";
    
        // Conexión a la base de datos y ejecución de la consulta
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
    
            // Establecer los valores de los parámetros en la consulta
            stmt.setBytes(1, furData);  // Establecer el valor para 'furData' (tipo BLOB)
            stmt.setString(2, logWrite);  // Establecer el valor para 'logWrite' (tipo VARCHAR)
            stmt.setInt(3, idHojaPruebas);  // Establecer el valor para 'id' (tipo INT)
    
            // Ejecutar la consulta de actualización
            int filasAfectadas = stmt.executeUpdate();
    
            // Si se afectaron filas, la actualización fue exitosa
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // En caso de error o si no se actualizaron filas
    }

    public static byte[] obtenerPdfFur(int idHojaPruebas) {
        // Consulta SQL para obtener el valor de 'furData' basándose en el id
        String consultaSelect = "SELECT furData FROM hoja_pruebas WHERE TESTSHEET = ?";
    
        // Conexión a la base de datos y ejecución de la consulta
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
    
            // Establecer el valor del parámetro en la consulta
            stmt.setInt(1, idHojaPruebas);
    
            // Ejecutar la consulta y procesar el resultado
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    // Retornar el contenido de 'furData' como byte[]
                    return resultSet.getBytes("furData");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; // En caso de error o si no se encuentra el registro
    }

    public static String obtenerRegistrosDeEscriturasFur(int idHojaPruebas) {
        // Consulta SQL para obtener el valor de 'logWrite' basado en el id
        String consultaSelect = "SELECT logWrite FROM hoja_pruebas WHERE TESTSHEET = ?";
    
        // Conexión a la base de datos y ejecución de la consulta
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
    
            // Establecer el parámetro para el id
            stmt.setInt(1, idHojaPruebas);
    
            // Ejecutar la consulta
            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    // Obtener el valor de logWrite
                    String logWrite = resultado.getString("logWrite");
                    return logWrite != null ? logWrite : ""; // Retornar cadena vacía si es nulo
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return ""; // Retornar cadena vacía en caso de error o si no se encontró el id
    }


    private static String nombreUsuario = "";

    public static String getDtName(String nickUsuario, String contrasenia){
        if (nombreUsuario.equals("")) {
            nombreUsuario = getUserFromDb(nickUsuario, contrasenia);
            return nombreUsuario;
        }
        return nombreUsuario;
    }

    public static String getDtName(){
        if (nombreUsuario.equals("")){
            CMensajes.mensajeError("Sucedio un error al tratar de tratar de acceder al DT de la solicitud. Contactese con soporte soltelec");
            throw new RuntimeException("No se puede acceder al usuario porque no se ha usado antes la funcion getDtName(String nickUsuario, String contrasenia) de Utils para verificar credenciales y registrar el nombre del dt");
        }
        return nombreUsuario;
    }

    private static String getUserFromDb(String nickUsuario, String contrasenia) {
        String query = "SELECT Nombre_usuario FROM usuarios WHERE Contrasenia = ? AND Nick_usuario = ?;";
        String name = "";
    
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(query)) {
    
            // Establecemos los parámetros de la consulta
            stmt.setString(1, contrasenia);
            stmt.setString(2, ".DT" + nickUsuario);  // Se usa "%" + nickUsuario + "%" para LIKE
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("Nombre_usuario");
                }
            }
    
        } catch (SQLException e) {
            System.err.println("Error al intentar obtener el nombre del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return name;
    }

    public static InfoHojaPruebas getInfoPruebas(Integer idHojaPruebas) {
        // Consulta SQL para obtener el valor de 'CARPLATE' basándose en 'TESTSHEET'
        String consultaSelect = 
            "SELECT hp.TESTSHEET, v.CARPLATE, hp.preventiva, hp.Numero_intentos, hp.Fecha_ingreso_vehiculo \n"+
            "FROM hoja_pruebas hp\n"+
            "INNER JOIN vehiculos v ON v.CAR = hp.Vehiculo_for\n"+
            "WHERE hp.TESTSHEET = ?\n";
    
        // Conexión a la base de datos y ejecución de la consulta
        try (Connection conexion = DriverManager.getConnection(Conexion.getUrl(), Conexion.getUsuario(), Conexion.getContrasena());
             PreparedStatement stmt = conexion.prepareStatement(consultaSelect)) {
    
            // Establecer el valor del parámetro en la consulta
            stmt.setInt(1, idHojaPruebas);
    
            // Ejecutar la consulta y procesar el resultado
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    // Retornar el valor de 'CARPLATE' como String
                    InfoHojaPruebas info = new InfoHojaPruebas();
                    info.setIdHojaPrueba(resultSet.getInt("TESTSHEET"));
                    info.setNumeroIntentos(resultSet.getInt("Numero_intentos"));
                    info.setPlaca(resultSet.getString("CARPLATE"));
                    info.setPreventiva(resultSet.getString("preventiva").equalsIgnoreCase("Y"));

                    Timestamp timestamp = resultSet.getTimestamp("Fecha_ingreso_vehiculo");
                    info.setFechaIngreso(timestamp.toLocalDateTime());
                    return info;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; // En caso de error o si no se encuentra el registro
    }

    public static String getRutaPdf(int idHojaPruebas) throws IOException{

        InfoHojaPruebas info = getInfoPruebas(idHojaPruebas);

        // Obtener la fecha ingreso vehiculo
        LocalDateTime fechaDeIngreso = info.getFechaIngreso();
        int año = fechaDeIngreso.getYear();
        String nombreMes = fechaDeIngreso.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        int día = fechaDeIngreso.getDayOfMonth();

        // Construir la ruta de la carpeta
        String directorioBase = "C:\\opt\\reportes_fur";
        String rutaCarpeta = String.format("%s\\%d\\%s\\%02d", directorioBase, año, nombreMes, día);

        // Crear las carpetas si no existen
        Files.createDirectories(Paths.get(rutaCarpeta));
        
        int intentos = info.getNumeroIntentos(); //Si tiene 2 intentos es reinspeccion, si tiene 1 es la primera vez
        String esPreventiva = info.isPreventiva() ? "-preventiva" : "-"+intentos; //si es preventiva no debe tener # de intentos
        
        // Construir el nombre del archivo PDF
        String destFileNamePdf = String.format("%s\\%s.pdf", rutaCarpeta, info.getPlaca()+esPreventiva);

        return destFileNamePdf;
    }
}
