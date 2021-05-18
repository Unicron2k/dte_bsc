from flask import Flask, render_template

from part1 import part1
from part2 import part2

app = Flask(__name__)

app.register_blueprint(part1, url_prefix='/part1')
app.register_blueprint(part2, url_prefix='/part2')


@app.route('/', methods=["GET"])
def index() -> 'html':
    return render_template('index.html')


if __name__ == '__main__':
    app.run(debug=True)
