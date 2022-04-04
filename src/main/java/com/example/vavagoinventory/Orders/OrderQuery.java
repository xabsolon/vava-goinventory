package com.example.vavagoinventory.Orders;

import com.example.vavagoinventory.DatabaseContextSingleton;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.codegen.maven.goinventory.tables.Orders;

import java.util.ArrayList;

import static org.jooq.codegen.maven.goinventory.Tables.ORDERS;
import static org.jooq.impl.DSL.param;

public class OrderQuery {
    public static ArrayList<Order> orders = new ArrayList<>();

    public static void getQuery() {
        orders.clear();
        DSLContext create = DatabaseContextSingleton.getContext();
        Result<Record> result = create.select().from(Orders.ORDERS).fetch();
        for (Record r:result) {
            Order order = new Order.OrderBuilder()
                    .o_id(r.get(ORDERS.O_ID))
                    .p_id(r.get(ORDERS.P_ID))
                    .quantity(r.get(ORDERS.QUANTITY))
                    .build();
            orders.add(order);
        }
        System.out.println(orders);
    }

    public static void insertQuery(Order order) {
        Integer p_id = order.getP_id();
        Integer quantity = order.getQuantity();
        DSLContext create = DatabaseContextSingleton.getContext();
        Record record = create.insertInto(ORDERS, ORDERS.P_ID, ORDERS.QUANTITY)
                .values(p_id, quantity)
                .returningResult(ORDERS.O_ID)
                .fetchOne();
        order.setO_id(record.get(ORDERS.O_ID));
    }

    public static void deleteQuery(int o_id) {
        Integer id = (Integer) o_id;
        System.out.println(id);
        DSLContext create = DatabaseContextSingleton.getContext();
        create.delete(ORDERS).where(ORDERS.O_ID.eq(id)).execute();
        //does not work for some reason, id in query gets replaced by a question mark
    }
}
