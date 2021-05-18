from flask import Blueprint, render_template, request
from Registers.BulletinRegister import BulletinRegister

part1 = Blueprint('part1', __name__, )
page_title = "Oblig3 Part 1"


@part1.route('/', methods=["GET"])
def index():
    sort = request.args.get('sort', type=int)
    if sort and sort > 0:
        entries = BulletinRegister().get_all_in_category(sort)
    else:
        entries = BulletinRegister().get_all()
    categories = BulletinRegister().get_all_categories()
    return render_template('bulletinBoard.html', board=entries, categories=categories, selectedCategory=sort,
                           page_title=page_title)


@part1.route('/<id>', methods=["GET"])
def view_entry(id):
    sort = request.args.get('sort', type=int)
    entry = BulletinRegister().get_entry(id)
    if entry:
        BulletinRegister.increment_hit(id)
        entry.hitcount += 1
    return render_template('bulletinEntry.html', entry=entry, selectedCategory=sort, page_title=page_title)
