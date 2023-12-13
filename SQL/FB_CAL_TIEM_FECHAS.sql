create or replace FUNCTION adm.FB_CAL_TIEM_FECHAS( FECHA_INICIAL date  , FECHA_FINAL date)
 RETURN VARCHAR2
 IS
 TIEMPO_TRANCURRIDO VARCHAR2(1000); 
 BEGIN
 SELECT  TO_CHAR(DIFERENCIA_HORAS,'00') ||':'|| TO_CHAR(DIFERENCIA_MINUTOS,'00') ||':'|| TO_CHAR(DIFERENCIA_SEGUNDOS,'00') 
        AS "TIEMPO TRANCURRIDO" INTO TIEMPO_TRANCURRIDO
FROM (
    SELECT FECHA_UNO,
        FECHA_DOS,
        TRUNC((FECHA_DOS - FECHA_UNO)) DIFERENCIA_DIAS,
        TRUNC(MOD((FECHA_DOS - FECHA_UNO) * 24, 24)) DIFERENCIA_HORAS,
        TRUNC(MOD((FECHA_DOS - FECHA_UNO) * (60 * 24), 60)) DIFERENCIA_MINUTOS,
        TRUNC(MOD((FECHA_DOS - FECHA_UNO) * (60 * 60 * 24), 60)) DIFERENCIA_SEGUNDOS
    FROM (
        SELECT 
         FECHA_INICIAL FECHA_UNO,
         FECHA_FINAL  FECHA_DOS
        FROM DUAL
        )

    );

    RETURN TIEMPO_TRANCURRIDO;
    END;