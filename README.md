# CLI-Command-Line-Interface (JAVA LANGUAGE)
</br>Implementing a simplified shell to allow executing some modification/query commands on a file system (Java Language + Command, Singleton, Factory patterns)
</br>Pislari Vadim -> Facultatea Automatica si Calculatoare -> Universitatea Politehnica Bucuresti

                                                     (Romanian Language)
                                                     -----Descriere-----
CLI-ul (Command  Line Interface) oferă o modalitate foarte eficientă de lucru cu fișiere (creare, copiere, mutare, listare, afișare etc.). Proiectul isi propunce implementarea unui shell simplificat care să permită execuția unor comenzi de modificare/interogare asupra unui sistem de fișiere.  Sistemul de fișiere poate fi imaginat sub forma unei structuri arborescente, cu noduri de tip fișier sau director, fiecare nod director putând avea 0 sau mai mulți fii. Nodul rădăcină are calea “/”. Se foloseste design pattern-ul Composite pentru modelarea structurii ierarhice. 

Comenzile pe care le suportă shell-ul sunt următoarele:    

●<i><b> ls "path"</i></b> - Listează fișierele și directoarele din folderul "path" sau din folderul curent dacă nu este dat niciun folder ca argument.   
</br> &nbsp;&nbsp;&nbsp; E1. Dacă "path" nu reprezintă un folder valid, se afișeaza eroarea “ls: "path": No such  directory”.  Dacă este urmată de argumentul -R, comanda listează întreg subarborele cu rădăcina în directorul curent (sau folderul "path", după caz). În acest caz, se  realizeaza o parcurgere  depth-first a subarborelui, alegând la fiecare pas nodurile în ordine lexicografică.

●<i><b> pwd</i></b> - Afișează calea absolută (pornind de la folderul “/”) a folderului curent.    

●<i><b> cd "path"</i></b> - Setează folderul curent la "path".   
</br> &nbsp;&nbsp;&nbsp; E1. Dacă acest folder nu există se afișeaza eroarea “cd: "path": No such directory”.    

●<i><b> cp "source" "dest_folder"</i></b> - Copiază fișierul/folderul dat prin calea "source" în folderul "dest_folder".   
</br> &nbsp;&nbsp;&nbsp; Notă: Dacă "source" reprezintă un folder, atunci se realizeaza o copiere recursivă (se copiaza și conținutul folderului "source"). 
</br> &nbsp;&nbsp;&nbsp; E1. Dacă path-ul "source" nu există, se afișeaza eroarea “cp: cannot copy "source": No  such file or directory”.   
</br> &nbsp;&nbsp;&nbsp; E2. Dacă path-ul "dest_folder" nu există, se afișeaza eroarea “cp: cannot copy into "dest_folder": No such directory”. 
</br> &nbsp;&nbsp;&nbsp; E3. Dacă folderul destinație conține deja un fișier sau folder cu numele fișierului/  folderului ce se dorește copiat, se afișeaza eroarea “cp: cannot copy "source": Node  exists at destination”, iar copierea nu are loc.    

●<i><b> mv "source" "dest_folder"</i></b> - Mută fișierul/folderul dat prin calea "source" la calea "dest_folder". 
</br> &nbsp;&nbsp;&nbsp; Nota de la cp se  aplică și aici.  
</br>  &nbsp;&nbsp;&nbsp;E1. Dacă path-ul "source" nu există, se afiseaza eroarea “mv: cannot move "source": No  such file or directory”.
</br> &nbsp;&nbsp;&nbsp; E2. Dacă path-ul "dest_folder" nu există, se afiseaza eroarea “mv: cannot move into "dest_folder": No such directory”.
</br> &nbsp;&nbsp;&nbsp; E3. Dacă folderul destinație conține deja un fișier sau folder cu numele fișierului/  folderului ce se dorește mutat, se afiseaza eroarea “mv: cannot move "source": Node  exists at destination”, iar mutarea nu are loc.  
</br> &nbsp;&nbsp;&nbsp; E4. Dacă se încearcă mutarea unui folder al cărui subarbore include current working  directory-ul, noul current working dir se  "muta" și el, păstrându-și poziția relativă față de folderul care se mută. 
  
●<i><b> rm "path" </i></b> - Șterge fișierul/folderul (cu tot cu conținutul său) de la locația "path".
</br> &nbsp;&nbsp;&nbsp; E1. Dacă fișierul/folderul nu există se afișeaza eroarea “rm: cannot remove "path": No  such file or directory”.
</br> &nbsp;&nbsp;&nbsp; E2. Dacă se încearcă ștergerea unui folder al cărui subarbore include current working  directory-ul, comanda nu are niciun efect.     

●<i><b> touch "file_path"</i></b> - Creează un nou fișier având calea "file_path".
</br> &nbsp;&nbsp;&nbsp; E1. Dacă folderul în care se dorește crearea fișierului nu există, se afiseaza: “touch: "parent_path": No such directory”, unde "parent_path" se obține din "file_path" prin  eliminarea token-ului ce reprezintă numele fișierului (Ex: /dir/file => /dir).   
</br> &nbsp;&nbsp;&nbsp;E2. Dacă un fișier/folder cu același nume există deja la calea dată de argument, se afișeaza eroarea “touch: cannot create file "file_absolute_path": Node exists”. 

●<i><b> mkdir "folder_path"</i></b> - Creează un nou folder având calea "folder_path".   
</br> &nbsp;&nbsp;&nbsp; E1. Dacă folderul în care se dorește crearea fișierului nu există, se va afiseaza eroarea:  “mkdir: "parent_path": No such directory”, unde "parent_path" se obține din "folder_path" prin  eliminarea token-ului ce reprezintă numele folderului ce se dorește creat.
</br> &nbsp;&nbsp;&nbsp;E2. Dacă un fișier/folder cu același nume există deja la calea dată de argument, se afiseaza eroarea “mkdir: cannot create directory "folder_absolute_path": Node exists”.
</br> &nbsp;&nbsp;&nbsp; Notă: în cazul erorilor E2 de la comenzile touch și mkdir, se afișeaza căile absolute și  complete (fără token-uri de forma “.”, “..” sau care conțin ‘*’ ) ale fișierului, respectiv folderului care se doresc create. 

● <i><b> grep "regex"</i></b>  este folosită în conjuncție cu ls printr-un pipe “|”. Aceasta limiteaza conținutul folderelor listate de ls (sau ls -R) doar la acele elemente care fac match pe expresia regulată "regex". Exemplu: ls / | grep “[a-z]*”. 

● Comenzile ls, rm, touch și mkdir aplicate pe path-uri ce pot  conține ‘*’ (reprezentând orice șir de caractere). 
  
  
                                                       ----Implementarea----
  Metoda de implementare a proiectului a fost crearea unui arbore reprezentat prin 2 tipuri de noduri: 
File (frunze) si Folder(subarbore), cu nodul principal (aflat in varful arborelui) "/", root. Fiecare 
folder are cate un arraylist cu toate nodurile copii ale acestui folder. Pentru a traversa acest arbore 
a fost creata o clasa Position cu o singura instanta (Singelton) care indica nodul actual la care se afla 
user-ul.
</br>  &nbsp;&nbsp;&nbsp;  A fost creata cate o clasa pentru fiecare comanda. Ideile si functionalitatile acestor clase sunt.
</br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Cd modifica nodul la care se afla Position cu nodul de la calea indicata. Se imparte 
path-ul in sectiuni care au la margini "/", si se modifica succesiv cu nodul care are denumirea data;
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	2. Mkdir determina numele nodului care trebuie creat si calea la care se afla, se muta la calea indicata si creaza un folder. 
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	3. Touch are aceiasi implementare ca Mkdir doar ca se creaza un File;
</br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4. Pwd afiseaza calea absoluta a nodului care il are setat Position. La fiecare creare a unui nod se seteaza un parametru care indica calea absoluta;
 </br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	5. Cp determina numele nodului care trebuie creat, calea sursei si calea destinatiei. Se face un cd catre sursa si altul catre destinatie, si se realizeaza o crearea recursiva in destinatie a 
tuturor nodurilor aflate in sursa;
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	6. Rm determina numele nodului care trebuie sters, calea catre el. Se face cd catre cale si se 
sterge din arraylistul cu copii nodul cu denumirea determinata;
</br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7. Mv realizeaza un cp cu aceiasi parametri, dupa care se realizeaza un rm pentru a se sterge sursa.
</br>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8. Ls realizeaza un cd catre calea primita dupa care afiseaza fie recusiv toate nodurile din arbore(-R),sau toate nodurile din araylist - ul cu copii.
