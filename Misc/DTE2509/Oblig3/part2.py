from flask import Blueprint, render_template, request, redirect, url_for

from Entities.BulletinEntry import BulletinEntry
from Registers.BulletinRegister import BulletinRegister

part2 = Blueprint('part2', __name__, )
page_title = "Oblig3 Part 2"

@part2.route('/', methods=["GET"])
def index():
    sort = request.args.get('sort', type=int)
    if sort and sort > 0:
        entries = BulletinRegister().get_all_in_category(sort)
    else:
        entries = BulletinRegister().get_all()
    categories = BulletinRegister().get_all_categories()
    return render_template('bulletinBoard2.html', board=entries, categories=categories, selectedCategory=sort, page_title=page_title)


@part2.route('/<id>', methods=["GET"])
def view_entry(id):
    sort = request.args.get('sort', type=int)
    entry = BulletinRegister().get_entry(id)
    if entry:
        BulletinRegister.increment_hit(id)
        entry.hitcount += 1
    return render_template('bulletinEntry.html', entry=entry, selectedCategory=sort, page_title=page_title)


@part2.route('/new', methods=["GET", "POST"])
def new():
    categories = BulletinRegister().get_all_categories()

    if request.method == "GET":
        return render_template('newEntry.html', categories=categories, page_title=page_title)

    if request.method == "POST":
        # Simple input validation:
        title = request.form['title'][0:50]
        category = int(request.form['category'])
        user = request.form['user'][0:12]
        ingress = request.form['ingress']
        content = request.form['content']

        invalid = False
        if not [cat for cat in categories if category in cat]:
            invalid = True
        if len(title) < 3:
            invalid = True
        if len(user) < 2:
            invalid = True
        if len(ingress) < 20:
            invalid = True
        if len(content) < 50:
            invalid = True
        if invalid:
            return render_template('newEntry.html', categories=categories, invalid=invalid, page_title=page_title)
        entry = BulletinEntry(None, title, ingress, content, user, None, None, category)
        BulletinRegister().create_new_entry(entry)
        return redirect(url_for("part2.index"))
    return redirect("/")
