import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerB extends UnicastRemoteObject implements Server {

    private static String ip;
    private static final String ipBroker = "155.210.154.209";
    private static final String brokerName = "Broker3675";
    private static String hostName = "ServerB3675";

    public ServerB(String ip) throws RemoteException {
        super();
        ServerB.ip = ip;
    }

    public String dar_nombre() {
		return "Doraemon";
    }

    public String dar_alias() {
		return "el gato cósmico";
    }

    public String ejecutar_metodo(String metodo, String[] tipoParametros, String retorno, Object[] parametros) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = this.getClass().getMethod(metodo, toClassArray(tipoParametros)); //El segundo argumento es los parametros
        return (String)method.invoke(this,parametros);
    }

    public static void main(String args[]) {
        System.setProperty("java.security.policy", "./java.policy");
        System.setSecurityManager(new SecurityManager());
        
        try {
            Broker broker = (Broker) Naming.lookup("//" + ipBroker + "/" + brokerName);
            ServerB o = new ServerB(args[0]);
            System.out.println("Creado!");

            // Registrar el objeto ServerA remoto
            Naming.rebind("//" + ip + "/"+ hostName, o);

            // Registrar el servidor en el broker
            broker.registrar_servidor(hostName,ip);
            System.out.println("Estoy registrado en el Broker!");
            
            broker.registrar_servicio(hostName, "dar_nombre", "string", new String[]{});
            broker.registrar_servicio(hostName, "dar_alias", "string", new String[]{});
            System.out.println("Servicios registrados");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private Class[] toClassArray(String[] param){
		Class[] output = new Class[param.length];
        int i = 0;
        for(String p: param) {
            switch (p) {
                case "String":
                    output[i++] = String.class;
                    break;
                
                case "Integer":
                    output[i++] = Integer.class;
                    break;
                
                case "Boolean":
                    output[i++] = Boolean.class;
                    break;
                
                case "Character":
                    output[i++] = Character.class;
                    break;
                
                default:
                    output[i++] = Void.class;
                    break;
            }
        }
		return output;
	}
}