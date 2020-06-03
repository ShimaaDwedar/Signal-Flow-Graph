package control;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;*/

import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;


import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.Color;
import javax.swing.JTextPane;
public class GUI extends JFrame {

	
	SignalFlowGraph sfg = new  SignalFlowGraph ();
	ArrayList<ArrayList<Integer>> out = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> strings = new ArrayList<ArrayList<Integer>>();
	Map map = new HashMap();
	int nodes;
	int edges;
	int counter = 0;
	int  from [];
	int to [];
	Object weight[];
	
	static Layout<String, String> layout;
	static VisualizationViewer<String, String> vv;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JButton btnNewButton_1;

	/**
	 * Launch the application.
	 */
	static GUI frame = new GUI();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
					frame.getContentPane().setLayout(null);
				    frame.setSize(740, 560);
				 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	public static void draw (ArrayList<ArrayList<Integer>> out, int []from, int []to,Object []weight)  {
		final Graph<String, String> g = getGraph(out, from, to,weight);
		vv = new VisualizationViewer<String, String>(layout);
        vv.setLocation(10, 75);
        FlowLayout flowLayout = (FlowLayout) vv.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        flowLayout.setVgap(5);
        flowLayout.setHgap(5);
       
        
        Transformer<String, String> transformer = new Transformer<String, String>()
        {
            @Override
            public String transform(String s)
            {
                return s;
            }
        };
        vv.getRenderContext().setVertexLabelTransformer(transformer);
        

        Transformer<String, String> transforme = new Transformer<String, String>()
        {
            @Override
            public String transform(String edgeName) { ;                
                return edgeName;
             }
        };
        vv.getRenderContext().setEdgeLabelTransformer( transforme);
        
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        vv.setSize(740, 300);
        vv.setBackground(frame.getBackground());
        frame.getContentPane().add(vv);
	}
	
	public static Graph<String, String> getGraph(ArrayList<ArrayList<Integer>> out, int []from, int []to,Object []weight) 
    {
        Graph<String, String> g = new DirectedSparseMultigraph<String, String>();
        layout = new StaticLayout(g);
		 Map map = new HashMap(); 
		 for (int i=0;i<from.length;i++) {
			 
			 map.put(String.valueOf(from[i])+String.valueOf(to[i]),weight[i] );
		 }
		 g.addVertex("v1");
		 Point p = new Point();
		 int pos =10;
		 int x = frame.getWidth()/(out.size()-1);
		 p.x=pos;
		 p.y=pos+100;
		 layout.setLocation("v1", p );
		 for (int i=2; i<=out.size()-1; i++)
        {			 
            g.addVertex("v"+i); 
            p.x=p.x+x;
            if (i%2 == 0) {
            	p.y = p.y+50+4*i;
            }else {
            	p.y = p.y-50-3*i;
            }
   		 	layout.setLocation("v"+i, p );
            if (out.get(i-1).size()>0&&out.get(i-1).contains(i)) {
            	String ft =String.valueOf(i-1)+String.valueOf(i);
            	String we = (String) map.get(ft);
            	while (g.getEdges().contains(we)) {
						we = we +" ";				
            	}
            g.addEdge(we, "v"+(i-1), "v"+i);
            }
        }
		
		for (int i=0; i<from.length; i++)
        {
			String s  ="e"+(i+1);
			if ((from[i]!= to[i]-1)) {
				String ft =String.valueOf(from[i])+String.valueOf(to[i]);
            	String we = (String) map.get(ft);
            	while (g.getEdges().contains(we)) {
					we = we +" ";				
            	}
				 g.addEdge(we, "v"+from[i], "v"+to[i]);
			}
			
           
        }
		
        return g;
    }

	/**
	 * Create the frame.
	 */
	public GUI() {
		setBackground(new Color(253, 245, 230));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 758, 499);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(253, 245, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nodes");
		lblNewLabel.setBounds(20, 10, 46, 13);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Edges");
		lblNewLabel_1.setBounds(20, 40, 46, 13);
		contentPane.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(62, 7, 35, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(62, 37, 35, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("From :");
		lblNewLabel_2.setBounds(215, 17, 46, 13);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("To :");
		lblNewLabel_3.setBounds(313, 17, 46, 13);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Weight :");
		lblNewLabel_4.setBounds(404, 17, 46, 13);
		contentPane.add(lblNewLabel_4);
		
		textField_2 = new JTextField();
		textField_2.setBounds(258, 14, 35, 19);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(342, 14, 35, 19);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(453, 14, 35, 19);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nodes = Integer.parseInt(textField.getText());
				edges = Integer.parseInt(textField_1.getText());
				 from = new int[edges];
				 to = new int[edges];
				 weight = new Object[edges];
				 
				sfg.setting(nodes, edges, out);
			}
		});
		btnNewButton.setBounds(107, 20, 64, 21);
		contentPane.add(btnNewButton);
		
		
		JTextPane textPane =  new JTextPane();
		textPane.setForeground(Color.WHITE);
		textPane.setBackground(Color.BLACK);
		textPane.setBounds(10, 396, 706, 117);
		contentPane.add(textPane);
		
		
		btnNewButton_1 = new JButton("Add");
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField_2.getText().contentEquals("")&&!textField_3.getText().contentEquals("")&&!textField_4.getText().contentEquals("")) {
				if (counter<edges) {
					from[counter] = Integer.parseInt(textField_2.getText());
					to[counter] = Integer.parseInt(textField_3.getText());
					weight [counter] = textField_4.getText();
					counter++;
				}
				if (counter == edges) {
					textField_2.setText("");
					textField_3.setText("");
					textField_4.setText("");
					ArrayList<String> loop = new ArrayList<String>();
						String temp = "• Over all TF = "+sfg.settingOut(out, edges, from, to, weight,loop);
						ArrayList<String> paths =  new ArrayList<String>();
						Map mapPath = new HashMap(); 
						ArrayList<String> strings =sfg.forwardPath(paths,out,1,"1",mapPath);
						String x = "• FrowardPaths: "+strings+'\n' ;
						strings = new ArrayList<String> ();
						String loops= "• Loops = "+loop;					
						x = x +loops+'\n'+temp;
					textPane.setText(x);
					draw (out, from, to,weight);
					btnNewButton_1.setEnabled(false);
				}
			}}
		});
		btnNewButton_1.setBounds(517, 13, 64, 21);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Clear");
		btnNewButton_2.setForeground(Color.WHITE);
		btnNewButton_2.setBackground(Color.BLACK);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){				
				vv.hide();
				textPane.setText("");
				textField.setText("");
				textField_1.setText("");
				btnNewButton_1.setEnabled(true);
			}
		});
		btnNewButton_2.setBounds(652, 13, 64, 21);
		contentPane.add(btnNewButton_2);
		
		
		
		
	}
}
