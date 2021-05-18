import mysql.connector


class MyDb:

    def __init__(self) -> None:
        dbconfig = {'host': 'kark.uit.no',
                    'user': 'stud_v19_eriksen',
                    'password': 'ThisismyKarkPassword1!$',
                    'database': 'stud_v19_eriksen',}
        self.configuration = dbconfig

    def __enter__(self) -> 'cursor':
        self.conn = mysql.connector.connect(**self.configuration)
        self.cursor = self.conn.cursor(prepared=True,)
        return self

    def __exit__(self, exc_type, exc_val, exc_trace) -> None:
        self.conn.commit()
        self.cursor.close()
        self.conn.close()

    def query(self, sql, data):
        self.cursor.execute(sql, data)
        result = self.cursor.fetchall()
        return result
