import java.io.*;
import java.util.ArrayList;



public class WriterReader {
    public static final long serialVersionUID = 1L;
    static BufferedReader dato = new BufferedReader(new InputStreamReader(System.in));
    private static File f = new File("clientes.dat");
    private static ArrayList<Cliente> personas = new ArrayList<>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opcion = 1;
        do{
            if(opcion <1 || opcion >6)
                System.err.println("Introduzca únicamente un valor entre 1 y 6");
            try{
                System.out.println("Menú:");
                System.out.println("1 - Añadir cliente");
                System.out.println("2 - Listar clientes");
                System.out.println("3 - Buscar cliente");
                System.out.println("4 - Borrar cliente");
                System.out.println("5 - Borrar fichero de clientes completamente");
                System.out.println("6 - Salir de la aplicación");
                System.out.println("Introduzca la opción(1-6): ");
                opcion = Integer.parseInt(dato.readLine());
                switch(opcion){
                    case 1:
                        introducirDatos();
                        break;
                    case 2:
                        verDatos();
                        break;
                    case 3:
                        buscarDato();
                        break;
                    case 4:
                        eliminarUno();
                        break;
                    case 5:
                        eliminarTodos();
                        break;
                    case 6:
                        System.out.println("GRACIAS POR UTILIZAR LA APLICACIÓN");
                        break;
                }
            }catch (NumberFormatException nfe){
                System.err.println("Sólo son válidos valores enteros entre 1 y 6");
            }catch(IOException ioe){
                System.err.println("Error de entrada de datos: "+ioe.getMessage());
            }
        }while(opcion !=6);
    }
    
    /****************
     * Introducimos los datos del cliente comprobando:
     * NIF      ->  Que tenga un valor entre 999999 y 99999999 y que termine en una letra
     * Teléfono ->  Que sean valores numéricos entre 600000000 y 700000000 o entre 900000000 y 1000000000
     * Deuda    ->  Que sea un valor numérico
     */
    
    //recibimos y comprobamos NIF del cliente
    private static void introducirDatos() {
        String nie = null;
        long tel = 0L;
        float deuda = 0F;
        boolean cantDeuda,numNIF,numTel;
        abrir();
        try{
            do{
                System.out.println("Introduzca el NIF del cliente: ");
                nie = dato.readLine();
                numNIF = compruebaNIF(nie);
                if(!numNIF)
                    System.err.println("NIF ERRÓNEO");
            }while(!numNIF);
            
        //-----------------------------------------------
        
        // recibimos el nombre del cliente
            System.out.println("Introduzca el nombre del cliente: ");
            String nombre = dato.readLine();
        //-----------------------------------------------
        
        // recibimos telefono del cliente
            do{
                numTel=false;
                try{
                    System.out.println("Introduzca el teléfono del cliente ");
                    tel = Long.parseLong(dato.readLine());
                    numTel = compruebaTel(tel);
                    if(!numTel)
                        System.err.println("Nº DE TELÉFONO ERRÓNEO");
                }catch(IOException | NumberFormatException e){
                    System.err.println("Introduzca números válidos");
                }
            }while(!numTel);
        //------------------------------------------------
            
        // recibimos direccion del cliente
            System.out.println("Introduzca la dirección del cliente: ");
            String direccion =dato.readLine();
            
        //-----------------------------------------------
        
        // recibimos deuda del cliente
            do{
                try{
                    cantDeuda = false;
                    System.out.println("Introduzca la deuda del cliente: ");
                    deuda = Float.parseFloat(dato.readLine());
                }catch(IOException | NumberFormatException e){
                    System.err.println("Introduzca números válidos");
                    cantDeuda = true;
                }
            }while(cantDeuda);
        //------------------------------------------    
            
            if(personas.isEmpty()){
                personas=new ArrayList<>();
            }
            personas.add(new Cliente(nie,nombre,tel,direccion,deuda));
            escribirArchivo();
            System.out.println("Cliente "+personas.size()+" añadido");
        }catch (IOException | NumberFormatException ex){
            System.err.println(ex.getMessage());
        }
    }

    /*****************
     * Función que nos permite visualizar todos los datos almacenados
     * Primero comprobamos que el fichero exista.
     * Si existe, lo abrimos y comprobamos que no este vacío recorriendo todo su contenido
     */
    private static void verDatos() {
        if(!f.exists()){
            System.err.println("NO EXISTEN DATOS");
        } else{
            abrir();
            if(personas != null){
                int con=1;
                for(Cliente p:personas){
                    System.out.println("Registro - "+p.toString());
                    con++;
                }
            }else{
                System.out.println("No existen clientes dados de alta.");
            }
        }
    }

    /*****************
     * Función que nos permite buscar un registro concreto buscándolo por su NIF
     * 
     */
    private static void buscarDato(){
        try{
           // String res;
           // boolean repetir;
            boolean encontrado;
           // do{
              //  repetir = false;
                System.out.println("Introduzca el nif del cliente que desea buscar");
                String nie = dato.readLine();
                abrir();
                int i=0;
                encontrado = false;
                for(Cliente p:personas){
                    if(p.getNif().equals(nie)){
                        encontrado = true;
                        System.out.println("Registro "+p.toString());
                    }
                    i++;
                }
                if(!encontrado)
                    System.err.println("REGISTRO NO ENCONTRADO");
            
            }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }

    /****************
     * Función similar a la anterior pero que en lugar de limitarse a mostrarnos el resultado
     * de la búsqueda, nos permite decidir si deseamos eliminar el registro encontrado
     */
    private static void eliminarUno() {
        try{
            System.out.println("Introduzca el nif del cliente que desea eliminar");
            String ni=dato.readLine();
            abrir();
            int i=0;
            boolean encontrado=false;
            for(Cliente p:personas){
                if(p.getNif().equals(ni)){
                    encontrado=true;
                    System.out.println("Registro - " +p.toString());
                    System.out.println("Está seguro que desea eliminarlo (S/N)");
                    String res;
                    do{
                        res=dato.readLine().toUpperCase();
                        if(!res.equals("N")&&!res.equals("S"))
                            System.err.println("Sólo 'S' para borrar y 'N' para mantenerlo");
                        if(res.equals("S")){
                            personas.remove(i);
                            escribirArchivo();
                            System.out.printf("REGISTRO ELIMINADO\n",i);
                        }
                    }while(!res.equals("S") && !res.equals("N"));
                }
                i++;
            }
            if(!encontrado)
                System.err.println("REGISTRO NO ENCONTRADO");
        } catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }

    /******************
     * Función que nos elimina el fichero de datos del disco
     */
    private static void eliminarTodos() {
         String res;
         boolean borrado=false;
         try{
            System.out.println("Está seguro que desea eliminar el fichero (S/N)");
            do{
                res=dato.readLine().toUpperCase();
                if(!res.equals("N")&&!res.equals("S"))
                    System.err.println("Sólo 'S' para borrar y 'N' para mantenerlo");
                if(res.equals("S")){
                    
                    borrado=f.delete();
                    if(borrado){
                        System.out.println("FICHERO DE DATOS ELIMINADO");
                        personas.clear();
                    }else{
                        System.err.println("No ha sido posible eliminar el fichero");
                    }
                }
            }while(!res.equals("S") && !res.equals("N"));
         }catch(FileNotFoundException fnf){
             System.err.println("Fichero inexistente: "+fnf.getMessage());
         }catch(Exception ex){
             ex.getMessage();
         }
    }

    /*****************
     * Función que nos abre el fichero de datos para cagar su contenido en el arreglo 'personas'.
     * Previamente comprueba si el fichero existe, y si es así carga su contenido en el ArrayList
     * y cierra el fichero. Si por cualquier motivo no se puede leer del disco (está creado pero no
     * contiene datos) nos avisa que el fichero está vacío.
     */
    private static void abrir(){
        try{
            if(!f.exists()){
                crearArchivo();
            }else{
                if(f.canRead()){
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    personas = (ArrayList<Cliente>)ois.readObject();
                    ois.close();
                    fis.close();
                }else{
                    System.err.println("FICHERO VACIO");
                }
            }
        }catch(IOException | ClassNotFoundException ex){
            System.err.println("Error: "+ex.getMessage());
        }
    }
    
    /***********************
     * Función que nos crea el fichero de datos (válido para cuando se comprueba que no existe)
     */
    private static void crearArchivo(){
        try{
            f.createNewFile();
            System.out.println("Fichero creado");
        }catch(Exception ex){
            System.err.println("ERROR: "+ex.getMessage());
        }
    }
     
    /************************
     * Función que nos escribe el ArrayList en el fichero de disco.
     */
    private static void escribirArchivo(){
        try{
            if(!f.exists()) f = new File("clientes.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(personas);
            oos.close();
            fos.close();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    
    /******************
     * Función que comprueba que el nif es correcto (nº entre 6 y 8 dígitos 
     * finalizados en una letra válida)
     * @param ni
     * @return boolean
     */
    private static boolean compruebaNIF(String ni) {
        boolean miNIF=false;
        int lon=ni.length();
        String letraValida="TRWAGMYFPDXBNJZSQVHLCKET";
        try{
            char letra=Character.toUpperCase(ni.charAt(lon-1));
            int numero=Integer.parseInt(ni.substring(0, lon-1));
            if((numero>999999 && numero<99999999) && (letra>=65 && letra<=90)){
                int numLetra = numero%23;
                if(letraValida.charAt(numLetra)!=letra){
                    System.err.println("Letra del NIF no válida");
                }else{
                    miNIF=true;
                }
            }
        }catch(Exception e){
            System.err.println("Numero NO válido");
        }
        return miNIF;
    }

    /*******************
     * Función que comprueba que el teléfono es válido comienza por 6 ó por 9 y tiene 9 dígitos
     * @param tel
     * @return boolean
     */
    private static boolean compruebaTel(long tel) {
        boolean miTEL=false;
        if((tel>600000000&&tel<700000000)||(tel>900000000&&tel<1000000000)) miTEL=true;
        return miTEL;
    }


}


