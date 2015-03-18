package templateparts;

public class ProductTemplatePartModel {

		private int productTemplateID, partID, partQuantity;
		
		public ProductTemplatePartModel(int ptid, int pid, int quantity) {
			setProductTemplateID(ptid);
			setPartID(pid);
			setQuantity(quantity);
		}
		
		public void setProductTemplateID(int ptid) {
			this.productTemplateID = ptid;
		}
		
		public void setPartID(int pid) {
			this.partID = pid;
		}
		
		public void setQuantity(int quantity) {
			this.partQuantity = quantity;
		}
		
		public int getProductTemplateID() {
			return this.productTemplateID;
		}
		
		public int getPartID() {
			return this.partID;
		}
		
		public int getPartQuantity() {
			return this.partQuantity;
		}
}
