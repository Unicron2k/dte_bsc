from Classes.MyDb import MyDb
from Entities.BulletinEntry import BulletinEntry


class BulletinRegister:

    def get_all(self):
        with MyDb() as db:
            result = db.query(
                '''select id,tittel,ingress,oppslagtekst,bruker,dato,treff,kategori.navn from oppslag join kategori on oppslag.kategori=kategori.kat_id''', ())
            return [BulletinEntry(*x) for x in result]

    def get_all_in_category(self, category):
        with MyDb() as db:
            result = db.query(
                '''select id,tittel,ingress,oppslagtekst,bruker,dato,treff,kategori.navn from oppslag join kategori on oppslag.kategori=kategori.kat_id where oppslag.kategori=%s''', (category,))
            return [BulletinEntry(*x) for x in result]

    def get_all_categories(self):
        with MyDb() as db:
            return db.query(
                '''select kategori.kat_id, kategori.navn from kategori''', ())

    def get_entry(self, id):
        with MyDb() as db:
            result = db.query('''select id,tittel,ingress,oppslagtekst,bruker,dato,treff,kategori.navn from oppslag join kategori on oppslag.kategori=kategori.kat_id where oppslag.id=%s;''', (id,))[0]
            return BulletinEntry(*result)

    @classmethod
    def increment_hit(cls, id):
        with MyDb() as db:
            return db.query(
                '''update oppslag set treff=treff + 1 where id=%s''', (id,))

    def create_new_entry(self, entry: BulletinEntry):
        with MyDb() as db:
            return db.query(
                '''INSERT INTO oppslag (id, kategori, tittel, ingress, oppslagtekst, bruker, dato, treff)
                VALUES (DEFAULT, %s, %s, %s, %s, %s, DEFAULT, DEFAULT);''',
                (entry.category, entry.title, entry.ingress, entry.content, entry.user))