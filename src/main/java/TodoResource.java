@javax.enterprise.context.RequestScoped
@javax.transaction.Transactional
@javax.persistence.PersistenceContext(name = "TodoEM")
@javax.ws.rs.Path("todo")
public class TodoResource {

    @javax.ws.rs.Path("list")
    @javax.ws.rs.GET
    @javax.ws.rs.Produces("application/json")
    public void list(
            @javax.ws.rs.container.Suspended javax.ws.rs.container.AsyncResponse response)
                    throws Exception {
        if (java.util.stream.Stream.of(0)
                .peek(unused -> run(s -> s
                        .map(em -> (java.util.List<Object[]>) em
                                .createNativeQuery(
                                        "select id, content, done from todo order by id")
                                .getResultList())
                        .map(list -> list.stream()
                                .map(a -> "{\"id\":" + a[0] + ",\"content\":\""
                                        + a[1] + "\",\"done\":" + a[2] + "}")
                                .collect(java.util.stream.Collectors
                                        .joining(",", "[", "]")))
                .peek(response::resume).count())).count() > 0) {
        }
    }

    @javax.ws.rs.Path("create")
    @javax.ws.rs.POST
    @javax.ws.rs.Consumes("application/json")
    public void create(javax.json.JsonObject todo,
            @javax.ws.rs.container.Suspended javax.ws.rs.container.AsyncResponse response)
                    throws Exception {
        if (java.util.stream.Stream.of(0).peek(unused -> run(s -> s
                .peek(em -> em
                        .createNativeQuery(
                                "insert into todo (content) values ('"
                                        + todo.getString("content") + "')")
                        .executeUpdate())
                .peek(em -> response
                        .resume(javax.ws.rs.core.Response.noContent().build()))
                .count())).count() > 0) {
        }
    }

    @javax.ws.rs.Path("update")
    @javax.ws.rs.POST
    @javax.ws.rs.Consumes("application/json")
    public void update(javax.json.JsonObject todo,
            @javax.ws.rs.container.Suspended javax.ws.rs.container.AsyncResponse response)
                    throws Exception {
        if (java.util.stream.Stream
                .of(0).peek(
                        unused -> run(s -> s
                                .peek(em -> em
                                        .createNativeQuery(
                                                "update todo set done = "
                                                        + todo.getBoolean(
                                                                "done")
                                                        + " where id = "
                                                        + todo.getInt("id"))
                                        .executeUpdate())
                                .peek(em -> response
                                        .resume(javax.ws.rs.core.Response
                                                .noContent().build()))
                                .count()))
                .count() > 0) {
        }
    }

    private void run(
            java.util.function.Consumer<java.util.stream.Stream<javax.persistence.EntityManager>> action) {
        if (java.util.stream.Stream
                .of((javax.persistence.EntityManager) ((java.util.function.Supplier) java.lang.reflect.Proxy
                        .newProxyInstance(getClass().getClassLoader(),
                                new Class[] {
                                        java.util.function.Supplier.class },
                                (proxy, method,
                                        args) -> javax.naming.InitialContext
                                                .doLookup(
                                                        "java:comp/env/TodoEM")))
                                                                .get())
                .peek(em -> em
                        .createNativeQuery(
                                "create table if not exists todo (id identity, content varchar(1000), done boolean default(false))")
                        .executeUpdate())
                .peek(em -> action.accept(java.util.stream.Stream.of(em)))
                .count() > 0) {
        }
    }
}
