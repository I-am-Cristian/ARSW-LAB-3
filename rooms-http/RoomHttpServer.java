import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RoomHttpServer {
    private static RoomRepository repository = new RoomRepository();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/", new RootHandler());
        server.createContext("/rooms", new RoomsHandler());
        server.createContext("/rooms/reserve", new ReserveHandler());
        server.createContext("/rooms/release", new ReleaseHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("RoomHttpServer iniciado en http://localhost:8080");
        System.out.println("Rutas disponibles:");
        System.out.println("  GET  /rooms              - Listar todos los salones");
        System.out.println("  GET  /rooms?id=E303      - Ver detalle de un salon");
        System.out.println("  POST /rooms/reserve?id=E303 - Reservar salon");
        System.out.println("  POST /rooms/release?id=E303 - Liberar salon");
    }

    // Handler para la raiz - redirige a /rooms
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><head><meta http-equiv='refresh' content='0;/rooms'></head></html>";
            sendResponse(exchange, 302, response, "text/html");
        }
    }

    // Handler para listar todos los salones
    static class RoomsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            
            if ("GET".equals(method)) {
                if (params.containsKey("id")) {
                    // Detalle de un salon especifico
                    String id = params.get("id");
                    Room room = repository.findById(id);
                    String response = generateRoomDetailHtml(room, id);
                    sendResponse(exchange, 200, response, "text/html");
                } else {
                    // Listar todos los salones
                    String response = generateRoomListHtml();
                    sendResponse(exchange, 200, response, "text/html");
                }
            } else {
                sendResponse(exchange, 405, "Metodo no permitido", "text/plain");
            }
        }
    }

    // Handler para reservar salon
    static class ReserveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod()) || "POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                String id = params.get("id");
                String user = params.getOrDefault("user", "Usuario Anonimo");
                
                if (id == null) {
                    String response = "Error: Se requiere el parametro 'id'";
                    sendResponse(exchange, 400, response, "text/plain");
                    return;
                }
                
                boolean success = repository.reserveRoom(id, user);
                String response;
                int statusCode;
                
                if (success) {
                    response = String.format("!Salon %s reservado exitosamente por %s!", id, user);
                    statusCode = 200;
                } else {
                    response = String.format("Error: No se pudo reservar el salon %s (puede estar ya reservado o no existir)", id);
                    statusCode = 409;
                }
                
                // Redirigir a la lista despues de 2 segundos
                String html = generateResultPage(response, "/rooms");
                sendResponse(exchange, statusCode, html, "text/html");
            } else {
                sendResponse(exchange, 405, "Metodo no permitido", "text/plain");
            }
        }
    }

    // Handler para liberar salon
    static class ReleaseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod()) || "POST".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                String id = params.get("id");
                
                if (id == null) {
                    String response = "Error: Se requiere el parametro 'id'";
                    sendResponse(exchange, 400, response, "text/plain");
                    return;
                }
                
                boolean success = repository.releaseRoom(id);
                String response;
                int statusCode;
                
                if (success) {
                    response = String.format("!Salon %s liberado exitosamente!", id);
                    statusCode = 200;
                } else {
                    response = String.format("Error: No se pudo liberar el salon %s (puede no estar reservado o no existir)", id);
                    statusCode = 409;
                }
                
                String html = generateResultPage(response, "/rooms");
                sendResponse(exchange, statusCode, html, "text/html");
            } else {
                sendResponse(exchange, 405, "Metodo no permitido", "text/plain");
            }
        }
    }

    // Metodo auxiliar para parsear query string
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null) return params;
        
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }

    // Generar HTML con lista de salones
    private static String generateRoomListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html><head><title>Gestion de Salones</title>");
        sb.append("<style>");
        sb.append("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
        sb.append("h1 { color: #333; }");
        sb.append("table { width: 100%; border-collapse: collapse; background: white; }");
        sb.append("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        sb.append("th { background-color: #ff0000; color: white; }");
        sb.append("tr:hover { background-color: #f5f5f5; }");
        sb.append(".reserved { background-color: #ffe6e6; }");
        sb.append(".available { background-color: #e6ffe6; }");
        sb.append("button { padding: 5px 10px; margin: 2px; cursor: pointer; }");
        sb.append(".status-available { color: green; font-weight: bold; }");
        sb.append(".status-reserved { color: red; font-weight: bold; }");
        sb.append("</style>");
        sb.append("</head><body>");
        sb.append("<h1>Gestion de Salones</h1>");
        sb.append("<table>");
        sb.append("<tr><th>ID</th><th>Nombre</th><th>Capacidad</th><th>Estado</th><th>Accion</th></tr>");
        
        for (Room room : repository.getAllRooms()) {
            sb.append(room.toHtmlRow());
        }
        
        sb.append("</table>");
        sb.append("<p><small>Tip: Tambien puedes usar curl o Postman para interactuar con la API</small></p>");
        sb.append("</body></html>");
        
        return sb.toString();
    }

    // Generar HTML con detalle de un salon
    private static String generateRoomDetailHtml(Room room, String id) {
        if (room == null) {
            return String.format(
                "<html><body><h1>Salon %s no encontrado</h1><a href='/rooms'>Volver</a></body></html>",
                id
            );
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><title>Detalle del Salon</title>");
        sb.append("<style>body { font-family: Arial; margin: 20px; }</style>");
        sb.append("</head><body>");
        sb.append("<h1>Detalle del Salon</h1>");
        sb.append("<p><strong>ID:</strong> ").append(room.getId()).append("</p>");
        sb.append("<p><strong>Nombre:</strong> ").append(room.getName()).append("</p>");
        sb.append("<p><strong>Capacidad:</strong> ").append(room.getCapacity()).append(" personas</p>");
        sb.append("<p><strong>Estado:</strong> ").append(room.isReserved() ? "Reservado" : "Disponible").append("</p>");
        if (room.isReserved()) {
            sb.append("<p><strong>Reservado por:</strong> ").append(room.getReservedBy()).append("</p>");
        }
        sb.append("<br><a href='/rooms'>Volver a la lista</a>");
        sb.append("</body></html>");
        
        return sb.toString();
    }

    // Generar pagina de resultado
    private static String generateResultPage(String message, String redirectUrl) {
        return String.format(
            "<!DOCTYPE html><html><head>" +
            "<meta http-equiv='refresh' content='2;url=%s'>" +
            "<style>body { font-family: Arial; text-align: center; margin-top: 50px; }</style>" +
            "</head><body>" +
            "<h2>%s</h2>" +
            "<p>Redirigiendo en 2 segundos...</p>" +
            "<a href='%s'>Haga clic aqui si no es redirigido</a>" +
            "</body></html>",
            redirectUrl, message, redirectUrl
        );
    }

    // Metodo auxiliar para enviar respuesta HTTP
    private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) 
            throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}