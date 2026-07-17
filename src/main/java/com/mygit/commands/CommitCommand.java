package com.mygit.commands;

import com.mygit.index.Index;
import com.mygit.objects.Commit;
import com.mygit.objects.Tree;
import com.mygit.refs.Ref;
import java.io.IOException;

public class CommitCommand {
    public static void run(String message) throws IOException {
        
        var index = Index.load();
        if(index.isEmpty()){
            System.out.println("No changes to commit.");
            return;
        }

        String treeHash = Tree.writeFromIndex(index);
        String parentHash = Ref.getHeadCommit();

        String commitHash = Commit.write(treeHash, parentHash, message);

        Ref.updateHead(commitHash);

        System.out.println("Committed as " + commitHash);
    }
}
