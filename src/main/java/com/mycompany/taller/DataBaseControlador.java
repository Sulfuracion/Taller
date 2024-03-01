package com.mycompany.taller;

import java.sql.*;

public class DataBaseControlador {
    //Para conectarse a la base de datos
    public Connection conexionDB(String bdname, String user, String contra){
        Connection conn= null;
        try {
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + bdname,user,contra);
            if(conn!=null){
                System.out.println("ta bueno");
            }else {
                System.out.println("no ta bueno");
            }
        }catch (Exception e){
            System.out.println("no ta bueno");
            System.out.println(e);
        }
        return conn;
    }
    
     public static void insertarVehiculo(Connection conn, String matricula, int potenciaCV, String transmision, String marca, String modelo, String seguro) {
        try {
            // Preparar la consulta SQL para insertar un nuevo vehículo
            // PostgreSQL permite el uso de la notación de fila para insertar en un tipo compuesto
            String sql = "INSERT INTO vehiculos(matricula, fichaTenica, marca, modelo, seguro) VALUES (?, ROW(?, ?)::fichaTecnicaTipo, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Establecer los valores de los parámetros
            pstmt.setString(1, matricula);
            pstmt.setInt(2, potenciaCV); // Parte de la ficha técnica
            pstmt.setString(3, transmision); // Parte de la ficha técnica, asegúrate de que coincide con los valores del ENUM
            pstmt.setString(4, marca); // Asegúrate de que coincide con los valores del ENUM
            pstmt.setString(5, modelo);
            pstmt.setString(6, seguro); // Asegúrate de que coincide con los valores del ENUM

            // Ejecutar la consulta
            int filasInsertadas = pstmt.executeUpdate();

            // Verificar si se insertó correctamente
            if (filasInsertadas > 0) {
                System.out.println("Vehículo insertado correctamente.");
            } else {
                System.out.println("Error al insertar vehículo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar vehículo: " + e.getMessage());
        }
    }
    
    
    public static void insertarCliente(Connection conn, String nombre, String direccion, String telefono) {
        try {
            // Preparar la consulta SQL para insertar un nuevo cliente
            String sql = "INSERT INTO clientes(nombre, tlfo, direccion) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Establecer los valores de los parámetros
            pstmt.setString(1, nombre);
            pstmt.setString(2, direccion);
            pstmt.setString(3, telefono);

            // Ejecutar la consulta
            int filasInsertadas = pstmt.executeUpdate();

            // Verificar si se insertó correctamente
            if (filasInsertadas > 0) {
                System.out.println("Cliente insertado correctamente.");
            } else {
                System.out.println("Error al insertar cliente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
    }
    
    
     public static void borrarClientePorId(Connection conn, int idCliente) {
        try {
            // Preparar la consulta SQL para borrar un cliente por su ID
            String sql = "DELETE FROM clientes WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Establecer el valor del parámetro ID
            pstmt.setInt(1, idCliente);

            // Ejecutar la consulta
            int filasBorradas = pstmt.executeUpdate();

            // Verificar si se borró correctamente
            if (filasBorradas > 0) {
                System.out.println("Cliente con ID " + idCliente + " borrado correctamente.");
            } else {
                System.out.println("No se encontró ningún cliente con ID " + idCliente + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al borrar cliente: " + e.getMessage());
        }
    }
    
    public static void actualizarCliente(Connection conn, int idCliente, String nombre, String direccion, String telefono) {
        try {
            // Preparar la consulta SQL para actualizar un cliente por su ID
            String sql = "UPDATE clientes SET nombre = ?, direccion = ?, tlfo = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Establecer los valores de los parámetros
            pstmt.setString(1, nombre);
            pstmt.setString(2, direccion);
            pstmt.setString(3, telefono);
            pstmt.setInt(4, idCliente); // El ID no se modifica, se utiliza en la cláusula WHERE

            // Ejecutar la consulta
            int filasActualizadas = pstmt.executeUpdate();

            // Verificar si se actualizó correctamente
            if (filasActualizadas > 0) {
                System.out.println("Cliente con ID " + idCliente + " actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún cliente con ID " + idCliente + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        }
    }
    
    
  
    
    
    
    
    
    
    
    
    
    //muestra toda la tabla documentos
    public static void mostrarDocumentos(Connection conn) {
        try {
            String sql = "SELECT * FROM documentos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String genero = rs.getString("genero");
                String autor = rs.getString("autor");
                String titulo = rs.getString("titulo");
                int stock = rs.getInt("stock");

                System.out.println("ISBN: " + isbn + ", Genero: " + genero + ", Autor: " + autor + ", Titulo: " + titulo + ", Stock: " + stock);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error al mostrar los documentos.");
            e.printStackTrace();
        }
    }
     
    //filtra por el titulo 
    public static void buscarDocumentosPorTitulo(Connection conn, String titulo) {
        try {
            String sql = "SELECT titulo, genero FROM documentos WHERE titulo LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tituloDocumento = rs.getString("titulo");
                String genero = rs.getString("genero");

                System.out.println("Título: " + tituloDocumento + ", Género: " + genero);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error al buscar documentos por título.");
            e.printStackTrace();
        }
    }
    
    
    //elimina un usuario por el identificador(isbn)
    public static void eliminarDocumentoPorISBN(Connection conn, int isbn) {
        try {
            String sql = "DELETE FROM documentos WHERE isbn = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, isbn);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Documento eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún documento con el ISBN especificado.");
            }

            stmt.close();
        } catch (Exception e) {
            System.out.println("Error al eliminar el documento.");
            e.printStackTrace();
        }
    }
    
    //se le pasa un identificador(isbn) y la cantidad de stock que se quiere establecer
    public static void modificarStockDocumento(Connection conn, int isbn, int nuevoStock) {
        try {
            String sql = "UPDATE documentos SET stock = ? WHERE isbn = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, isbn);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cantidad de stock modificada correctamente.");
            } else {
                System.out.println("No se encontró ningún documento con el ISBN especificado.");
            }

            stmt.close();
        } catch (Exception e) {
            System.out.println("Error al modificar la cantidad de stock del documento.");
            e.printStackTrace();
        }
    }
    
    //muestra la tabla documentos registrados filtrando por el identificador(codigoEmpleado)
    public static void mostrarDocumentosRegistrados(Connection conn, int codigoEmpleado) {
        try {
            String sql = "SELECT documentosRegistrados FROM empleados WHERE cod = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, codigoEmpleado);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int documentosRegistrados = rs.getInt("documentosRegistrados");
                System.out.println("Documentos registrados del empleado: " + documentosRegistrados);
            } else {
                System.out.println("No se encontró ningún empleado con el código especificado.");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error al mostrar los documentos registrados del empleado.");
            e.printStackTrace();
        }
    }
         
    
    
   
}
