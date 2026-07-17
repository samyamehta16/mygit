# MyGit — a Git implementation in Java

A from-scratch reimplementation of Git's core internals, built to understand how content-addressable storage, snapshots, and version history actually work under the hood — not just how to call `git` commands.

## What it does

- **`init`** — sets up the repository structure (`.mygit/objects`, `.mygit/refs`, `HEAD`)
- **`hash-object` / `cat-file`** — content-addressable blob storage: every piece of content is SHA-1 hashed, zlib-compressed, and stored by its hash
- **`write-tree`** — recursively snapshots a directory into a single tree hash
- **`add` / `status`** — a staging index tracking what's staged, modified, or untracked
- **`commit` / `log`** — commits as immutable snapshots linked to their parent, forming full history
- **`branch` / `checkout`** — lightweight branch pointers with working-directory restore
- **`diff`** — line-level diffing between staged and working versions using an LCS-based algorithm

## How it works

Git's core insight is that it never really tracks "files changing" — it stores immutable **snapshots**, addressed by the hash of their own content. This project reimplements that model directly:

- **Blobs** are just file contents, hashed and compressed. Identical content always produces the identical hash, so identical files are automatically deduplicated.
- **Trees** are lists of `(type, hash, name)` entries representing a directory. A tree can point to blobs (files) or other trees (subdirectories), so one hash can represent an entire nested folder structure.
- **Commits** are just a pointer to one tree (the snapshot) plus a pointer to a parent commit (the previous snapshot). Walking `parent` pointers backward from `HEAD` reconstructs the full project history.
- **Branches** are nothing more than a named file containing a commit hash. `checkout` moves `HEAD` to a different branch and rewrites the working directory to match that branch's tree.

Every object type (blob, tree, commit) is stored through the same underlying mechanism: label it, hash it, compress it, write it to `.mygit/objects/<first 2 chars>/<remaining chars>`. That reuse is deliberate — it mirrors how Git itself is designed.

## Try it

```bash
mvn compile

java -cp target/classes com.mygit.Main init
java -cp target/classes com.mygit.Main add somefile.txt
java -cp target/classes com.mygit.Main commit -m "first commit"
java -cp target/classes com.mygit.Main log

java -cp target/classes com.mygit.Main branch feature
java -cp target/classes com.mygit.Main checkout feature

java -cp target/classes com.mygit.Main diff somefile.txt
```

## Tests

```bash
mvn test
```

Covers deterministic hashing, blob round-tripping (write then read returns original content), and diff correctness.

## What's not implemented (yet)

- Three-way merge between branches
- Remote push/pull over a network protocol
- Full working-directory reconciliation on checkout — files left over from a different branch aren't currently deleted on switch
- A packfile-style storage format for efficiency (objects are stored loose, one per file, as Git does before garbage collection)

## Project structure

```
src/main/java/com/mygit/
├── Main.java              # CLI entry point and command dispatch
├── commands/               # one class per command (init, add, commit, etc.)
├── objects/                 # GitObject (storage engine), Blob, Tree, Commit
├── index/                    # staging area
├── refs/                     # branch/HEAD pointer management
├── diff/                      # LCS-based line diffing
└── util/                       # SHA-1 hashing helper
```

## Why I built this

To genuinely understand what Git is doing under the hood rather than only knowing the commands — content-addressable storage, the blob/tree/commit object model, and how something as simple as a hash-linked list of snapshots gives you a full version control system.
