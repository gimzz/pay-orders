#!/bin/bash

CONTAINER_NAME="pay-orders-db-1"
DB_USER="admin"
DB_NAME="pay_orders_db"
BACKUP_DIR="./backup"
BACKUP_FILE="$BACKUP_DIR/system_backup.sql"
SCHEMA_NAME="system"

mkdir -p "$BACKUP_DIR"

docker exec "$CONTAINER_NAME" /bin/sh -c "pg_dump -U $DB_USER -d $DB_NAME -n $SCHEMA_NAME > /tmp/system_backup.sql"

docker cp "$CONTAINER_NAME":/tmp/system_backup.sql "$BACKUP_FILE"

docker exec "$CONTAINER_NAME" rm /tmp/system_backup.sql

echo "Backup creado y actualizado en $BACKUP_FILE"
