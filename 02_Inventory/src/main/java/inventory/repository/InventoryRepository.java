package inventory.repository;

import inventory.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.StringTokenizer;

public class InventoryRepository {

	private static String filename = "data/items.txt";
	private Inventory inventory = new Inventory();
	private static InventoryRepository repositoryInstance;
	public InventoryRepository() {}
	public static InventoryRepository getInstance() {
		if (repositoryInstance == null) {
			repositoryInstance = new InventoryRepository();
		}
		return repositoryInstance;
	}


	public void readParts(){
		File file = new File(filename);
		ObservableList<Part> listP = FXCollections.observableArrayList();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line=br.readLine())!=null){
				Part part=getPartFromString(line);
				if (part!=null)
					listP.add(part);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inventory.setAllParts(listP);
	}

	private Part getPartFromString(String line){
		Part item=null;
		if (line==null|| line.equals("")) return null;
		StringTokenizer st=new StringTokenizer(line, ",");
		String type=st.nextToken();
		if (type.equals("I")) {
			int id= Integer.parseInt(st.nextToken());
			inventory.setAutoPartId(id);
			String name= st.nextToken();
			double price = Double.parseDouble(st.nextToken());
			int inStock = Integer.parseInt(st.nextToken());
			int minStock = Integer.parseInt(st.nextToken());
			int maxStock = Integer.parseInt(st.nextToken());
			int idMachine= Integer.parseInt(st.nextToken());
			item = new InhousePart(id, name, price, inStock, minStock, maxStock, idMachine);
		}
		if (type.equals("O")) {
			int id= Integer.parseInt(st.nextToken());
			inventory.setAutoPartId(id);
			String name= st.nextToken();
			double price = Double.parseDouble(st.nextToken());
			int inStock = Integer.parseInt(st.nextToken());
			int minStock = Integer.parseInt(st.nextToken());
			int maxStock = Integer.parseInt(st.nextToken());
			String company=st.nextToken();
			item = new OutsourcedPart(id, name, price, inStock, minStock, maxStock, company);
		}
		return item;
	}

	public void readProducts(){
		//ClassLoader classLoader = InventoryRepository.class.getClassLoader();
		File file = new File(filename);

		ObservableList<Product> listP = FXCollections.observableArrayList();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line=br.readLine())!=null){
				Product product=getProductFromString(line);
				if (product!=null)
					listP.add(product);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inventory.setProducts(listP);
	}

	private Product getProductFromString(String line){
		Product product=null;
		if (line==null|| line.equals("")) return null;
		StringTokenizer st=new StringTokenizer(line, ",");
		String type=st.nextToken();
		if (type.equals("P")) {
			int id= Integer.parseInt(st.nextToken());
			inventory.setAutoProductId(id);
			String name= st.nextToken();
			double price = Double.parseDouble(st.nextToken());
			int inStock = Integer.parseInt(st.nextToken());
			int minStock = Integer.parseInt(st.nextToken());
			int maxStock = Integer.parseInt(st.nextToken());
			String partIDs=st.nextToken();

			StringTokenizer ids= new StringTokenizer(partIDs,":");
			ObservableList<Part> list= FXCollections.observableArrayList();
			while (ids.hasMoreTokens()) {
				String idP = ids.nextToken();
				Part part = inventory.lookupPart(idP);
				if (part != null)
					list.add(part);
			}
			product = new Product(id, name, price, inStock, minStock, maxStock, list);
			product.setAssociatedParts(list);
		}
		return product;
	}

	public void writeAll() {

		//ClassLoader classLoader = InventoryRepository.class.getClassLoader();
		File file = new File(filename);

		BufferedWriter bw = null;
		ObservableList<Part> parts=inventory.getAllParts();
		ObservableList<Product> products=inventory.getProducts();

		try {
			bw = new BufferedWriter(new FileWriter(file));
			for (Part p:parts) {
				System.out.println(p.toString());
				bw.write(p.toString());
				bw.newLine();
			}

			for (Product pr:products) {
				String line=pr.toString()+",";
				ObservableList<Part> list= pr.getAssociatedParts();
				int index=0;
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < list.size(); i++) {
					builder.append(list.get(i).getPartId());
					if (i < list.size() - 1) {
						builder.append(":");
					}
				}
				if (index==list.size()-1) {
					line+=builder.toString();
				} else {
					line+=builder.toString();
				}
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPart(Part part) throws IllegalArgumentException{
		if (part.getMax() < part.getInStock()) {
			throw new IllegalArgumentException("Max cannot be less than In Stock");
		} else if (part.getMin() > part.getMax()) {
			throw new IllegalArgumentException("Min cannot be greater than Max");
		} else {
			inventory.addPart(part);
			writeAll();
		}
	}

	public void addProduct(Product product){
		inventory.addProduct(product);
		writeAll();
	}

	public int getAutoPartId(){
		return inventory.getAutoPartId();
	}

	public int getAutoProductId(){
		return inventory.getAutoProductId();
	}

	public ObservableList<Part> getAllParts(){
		return inventory.getAllParts();
	}

	public ObservableList<Product> getAllProducts(){
		return inventory.getProducts();
	}

	public Part lookupPart (String search){
		return inventory.lookupPart(search);
	}

	public Product lookupProduct(String searchItem) {
		boolean isFound = false;
		boolean isMatch = false;
		Product foundProduct = null;

		for (Product p : inventory.getProducts()) {
			if (p.getName().contains(searchItem)) {
				isMatch = true;
				foundProduct = p;
				break;
			}
			if (String.valueOf(p.getProductId()).equals(searchItem)) {
				isMatch = true;
				foundProduct = p;
			}
		}

		if (isFound && isMatch) {
			return new Product(0, null, 0.0, 0,  0, 0, null);
		} else if (isFound && foundProduct.getInStock() <= 0) {
			return new Product(foundProduct.getProductId(), foundProduct.getName(),  0.0,  0,  0, 0, foundProduct.getAssociatedParts());
		}

		return foundProduct;
	}

	public void updatePart(int partIndex, Part part){
		inventory.updatePart(partIndex, part);
		writeAll();
	}

	public void updateProduct(int productIndex, Product product){
		inventory.updateProduct(productIndex, product);
		writeAll();
	}

	public void deletePart(Part part){
		inventory.deletePart(part);
		writeAll();
	}
	public void deleteProduct(Product product){
		inventory.removeProduct(product);
		writeAll();
	}

	public Inventory getInventory(){
		return inventory;
	}

	public void setInventory(Inventory inventory){
		this.inventory=inventory;
	}




}
