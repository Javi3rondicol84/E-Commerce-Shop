package com.store.store.helper;
import com.store.store.entity.ProductEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class GenericHttpHelper<T> {

    public ResponseEntity<?> getAllItemsResponse(List<T> items, String entityName) {
        if(items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo encontrar "+entityName);
        }

        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    public ResponseEntity<?> getItemByIdResponse(T item, Long id, String entityName) {
        if(item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el "+entityName+" con el id: " + id);
        }

        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

    public ResponseEntity<?> getPostResponse(T item, String entityName) {
        if (item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo añadir el nuevo "+entityName);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Item añadido correctamente");
    }

    public ResponseEntity<?> getPutResponse(T item, Long id, String entityName) {
        if(item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo actualizar el "+entityName+" con el id: " + id);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(item);
    }

    public ResponseEntity<?> getDeleteResponse(T item, Long id, String entityName) {
        if (item == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo eliminar el "+entityName+" con el id: " + id);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("El item con el id: " + id + " se eliminó correctamente");
    }

    public ResponseEntity<?> getItemsByLimitResponse(List<T> items, Integer limit, String entityName) {
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron más de " + limit + " " + entityName);
        }

        return ResponseEntity.status(HttpStatus.OK).body(items);
    }
}
