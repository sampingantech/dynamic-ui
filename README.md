# dynamic-ui
A dynamic-ui library to generate view based on JSON Schema draft 07

# How to test locally (indenpendent project)
1. git clone this repo (remember its cloned path)
1. In the Android Studio, Click `New` -> `Import Module` (pointing to cloned path in no. 1)
1. Change gradle to `implementation(project(':dynamic-ui'))`
1. Voila, you can test it locally and make changes as well
1. Please make sure not to include this library on your repository, better to add it as a git submodule (but this is for advanced git user)

# I want to contribute/bugfix/refactor (local library/git submodule)
1. git clone of course
1. `(your project)$: git submodule update --init --recursive --remote --rebase`
1. (**OPTIONAL** if the above step doesn't work) `(your project)$: git submodule foreach git pull --rebase origin <master|develop>` choose `develop` for latest snapshot
1. make changes inside `:dynamic-ui` and push it to github as separate repository from your main repo
1. TL;DR: https://stackoverflow.com/a/55570998/3763032 (explain about git submodule is on detached **HEAD**)

